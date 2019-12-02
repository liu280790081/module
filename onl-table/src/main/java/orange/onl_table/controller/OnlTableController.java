package orange.onl_table.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.db.service.DbService;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.dto.TableModel;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;
import orange.onl_table.service.OnlTableFieldService;
import orange.onl_table.service.OnlTableHeadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "表单生成")
@Slf4j
@RestController
@RequestMapping({"/table/api", "/table/head"})
public class OnlTableController {

    @Autowired
    private OnlTableHeadService headService;
    @Autowired
    private OnlTableFieldService fieldService;
    @Autowired
    private DbService dbService;

    @ApiOperation(value = "添加、编辑表单", notes = "添加、编辑表单")
    @PutMapping({"/addAll", "/editAll"})
    public boolean addAll(@RequestBody @Valid TableModel model, HttpServletRequest req) {
        boolean isAdd = req.getRequestURI().contains("addAll");
        OnlTableHead head = model.getHead();
        String tableName = head.getTableName();
        LambdaQueryWrapper<OnlTableHead> wrapper = new LambdaQueryWrapper<OnlTableHead>().eq(OnlTableHead::getTableName, tableName);
        if (isAdd) {
            if (dbService.checkDBTableExist(tableName)) {
                throw new OnlException("数据库表[" + tableName + "]已存在,请从数据库导入表单");
            }
        } else {
            if (StringUtils.isBlank(head.getId())) {
                throw new OnlException("表ID不存在");
            }
            long count = headService.count(new LambdaQueryWrapper<OnlTableHead>().eq(OnlTableHead::getId, head.getId()));
            if (0 == count) {
                throw new OnlException("实体不存在");
            }
            wrapper.notIn(OnlTableHead::getId, head.getId());
        }

        if (!tableName.matches("^[a-z0-9A-Z_]+$")) {
            throw new OnlException("表名只能是字母、数字、下划线组合");
        }
        if (headService.count(wrapper) > 0) {
            throw new OnlException("表[" + tableName + "]已存在");
        }
        if (StringUtils.isNotBlank(head.getTombstoneField())) {
            model.getFields().stream()
                    .filter(f -> head.getTombstoneField().equals(f.getDbFieldName()))
                    .findAny()
                    .orElseThrow(() -> new OnlException("未找到逻辑删除所对应的字段"));
        }
        long keyCount = model.getFields().stream()
                .filter(a -> {
                    if (StringUtils.isBlank(a.getDbFieldName())) {
                        throw new NullPointerException("字段名不能为空！！！");
                    }
                    if (!a.getDbFieldName().matches("^[a-z0-9A-Z_]+$")) {
                        throw new OnlException("字段[" + a.getDbFieldName() + "]只能是字母、数字、下划线组合");
                    }
                    boolean res = Objects.equals(CommonConstant.IS_TRUE, a.getDbIsKey());
                    if (res && (a.getDbIsNull() == null || 1 == a.getDbIsNull())) {
                        throw new OnlException("主键不能为空");
                    }
                    if (res && !StringUtils.isAnyBlank(a.getMainTable(), a.getMainField())) {
                        throw new OnlException("表[" + tableName + "] 主键不能关联外键");
                    }
                    return res;
                }).count();
        if (keyCount == 0) {
            throw new OnlException("表字段必须有一个主键");
        }
        return isAdd ? headService.addAll(model) : headService.editAll(model);
    }

    @ApiOperation(value = "同步表单", notes = "同步表单")
    @PostMapping({"/doDbSync/{code}/{synMethod}"})
    public boolean doDbSync(@PathVariable("code") String code, @PathVariable("synMethod") String synMethod) throws Exception {
        if ("force".equals(synMethod)) {
            throw new OnlException("暴力同步风险大，请使用正常模式");
        }
        headService.doDbSync(code, synMethod);
        return true;
    }

    @ApiOperation(value = "同步表单", notes = "同步表单")
    @PostMapping({"/doDbSyncAll"})
    public boolean doDbSyncAll() throws Exception {
        headService.doDbSyncAll();
        return true;
    }


    @ApiOperation(value = "删除、移除表", notes = "删除、移除表")
    @DeleteMapping({"/delete", "/removeRecord"})
    public boolean a(HttpServletRequest req, @RequestBody List<String> codes) throws OnlException, SQLException {
        boolean isDelete = req.getRequestURI().contains("/delete");
//        if (isDelete) {
//            throw new OnlException("删除表风险大，请先使用移除表！！！");
//        }

        headService.deleteRecordAndTable(codes, isDelete);
        return true;
    }

    @ApiOperation(value = "获取列表字段", notes = "获取列表字段")
    @GetMapping({"/getColumns/{code}"})
    public Map<String, Object> getColumns(@PathVariable("code") String code) {
        OnlTableHead head = headService.requireOne(code);

        LambdaQueryWrapper<OnlTableField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OnlTableField::getHeadId, head.getId()).eq(OnlTableField::getIsShowList, 1);
        wrapper.orderByAsc(OnlTableField::getOrderNum);
        List<OnlTableField> fields = fieldService.list(wrapper);

        ArrayList var7 = new ArrayList();
        for (OnlTableField field : fields) {
            if (CommonConstant.IS_TRUE.equals(field.getDbIsKey())) continue;
            HashMap var12 = new HashMap(3);
            var12.put("title", field.getDbFieldTxt());
            var12.put("dataIndex", field.getDbFieldNameOld());
            String var24 = field.getDbType();
            if ("int".equals(var24) || "double".equals(var24) || "BigDecimal".equals(var24) || "Date".equals(var24)) {
                var12.put("sorter", true);
            }
            var7.add(var12);
        }

        HashMap var16 = new HashMap();
        var16.put("columns", var7);
        var16.put("dictOptions", new HashMap());
        var16.put("formTemplate", head.getFormTemplate());
        var16.put("description", head.getTableTxt());
        var16.put("currentTableName", head.getTableName());
        if (CommonConstant.IS_TRUE_STR.equals(head.getIsTree())) {
            var16.put("pidField", head.getTreeParentIdField());
            var16.put("hasChildrenField", head.getTreeIdField());
            var16.put("textField", head.getTreeSingleRow());
        }
        return var16;
    }

    @ApiOperation(value = "获取表信息", notes = "获取表信息")
    @GetMapping({"/getFormItem/{code}"})
    public Map<String, Object> getFormItem(@PathVariable("code") String code) {
        OnlTableHead head = headService.requireOne(code);

        // 所属附表
        List scheduleList = headService.queryContainFKScheduleList(code).stream()
                .map(h -> {
                    JSONObject json = new JSONObject();
                    json.put("id", h.getId());
                    json.put("modelCode", h.getModelCode());
                    json.put("tableName", h.getTableName());
                    json.put("tableType", h.getTableType());
                    json.put("tableTxt", h.getTableTxt());
                    return json;
                }).collect(Collectors.toList());

        JSONObject var5 = new JSONObject();
        var5.put("head", head);
        var5.put("scheduleList", scheduleList);
        return var5;
    }

    @ApiOperation(value = "获取表查询条件", notes = "获取表查询条件")
    @GetMapping({"/getQueryInfo/{code}"})
    public List getQueryInfo(@PathVariable("code") String code) throws Exception {
        headService.requireOne(code);
        LambdaQueryWrapper<OnlTableField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OnlTableField::getHeadId, code).eq(OnlTableField::getIsQuery, 1);
        wrapper.orderByAsc(OnlTableField::getOrderNum);
        List var3 = fieldService.list(wrapper);

        ArrayList var4 = new ArrayList();
        int var5 = 0;
        HashMap var8;
        for (Iterator var6 = var3.iterator(); var6.hasNext(); var4.add(var8)) {
            OnlTableField var7 = (OnlTableField) var6.next();
            var8 = new HashMap();
            var8.put("label", var7.getDbFieldTxt());
            var8.put("view", var7.getFieldShowType());
            var8.put("mode", "group".equals(var7.getQueryMode()) ? "group" : "single");
            var8.put("field", var7.getDbFieldName());
            var8.put("dict", var7.getDictField());
            ++var5;
            if (var5 > 2) {
                var8.put("hidden", "1");
            }
        }

        return var4;
    }

    @ApiOperation(value = "获取onl表集合", notes = "获取onl表集合")
    @PostMapping({"/list", "/page"})
    public IPage list(@RequestBody JSONObject param) {
        Integer pageNo = Optional.ofNullable(param.getInteger("pageNo")).orElse(1);
        Integer pageSize = Optional.ofNullable(param.getInteger("pageSize")).orElse(30);

        LambdaQueryWrapper<OnlTableHead> wrapper = new LambdaQueryWrapper<>();
        if (param.containsKey("tableType") && !param.getJSONArray("tableType").contains(-1)) {
            wrapper.in(OnlTableHead::getTableType, param.getJSONArray("tableType"));
        }
        if (param.containsKey("tableName") && StringUtils.isNotBlank(param.getString("tableName"))) {
            String tableName = param.getString("tableName");
            wrapper.and(w -> w.like(OnlTableHead::getTableName, tableName)
                    .or().like(OnlTableHead::getTableTxt, tableName)
                    .or().like(OnlTableHead::getId, tableName));
        }
        wrapper.orderByDesc(OnlTableHead::getCreateTime);
        return headService.page(new Page<>(pageNo, pageSize), wrapper);
    }

    @ApiOperation(value = "获取onl主表集合", notes = "获取onl主表集合")
    @PostMapping({"/primaryList"})
    public IPage primaryList(@RequestBody JSONObject param) {
        param.put("tableType", new int[]{0, 1});
        return this.list(param);
    }

    @ApiOperation(value = "获取onl附表集合", notes = "获取onl附表集合")
    @PostMapping({"/scheduleList/{code}"})
    public List scheduleList(@PathVariable("code") String code) {
        return headService.queryContainFKScheduleList(code);
    }

    @ApiOperation(value = "获取数据库表", notes = "获取数据库表")
    @GetMapping({"/queryTables"})
    public List queryTables(@RequestParam(value = "keyword", required = false) String keyword) {
        return dbService.queryDBTableList(keyword);
    }

    @ApiOperation(value = "转换数据库表", notes = "转换数据库表")
    @PostMapping({"/transTables"})
    public boolean transTables(@RequestBody @Valid @NotEmpty String[] tableNames) throws Exception {
        headService.dbTableToOnl(tableNames);
        return true;
    }

}
