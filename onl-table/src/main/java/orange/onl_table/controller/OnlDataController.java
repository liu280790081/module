package orange.onl_table.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.util.excel.ExcelField;
import orange.onl_table.common.util.excel.ExcelFieldImpl;
import orange.onl_table.common.util.excel.ExportExcel;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;
import orange.onl_table.service.OnlDataService;
import orange.onl_table.service.OnlTableFieldService;
import orange.onl_table.service.OnlTableHeadService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "表单生成")
@Slf4j
@RestController
@RequestMapping({"/table/api"})
public class OnlDataController {

    @Autowired
    private OnlDataService dataService;

    @Autowired
    private OnlTableHeadService headService;

    @Autowired
    private OnlTableFieldService fieldService;

    @ApiOperation(value = "获取表数据集合", notes = "获取表数据集合")
    @PostMapping({"/getData/{code}", "/data/tree/{code}"})
    public Map<String, Object> getData(@PathVariable("code") String code, @RequestBody JSONObject param) throws Exception {
        long total = 0;
        List<Map<String, Object>> records = new ArrayList<>();
        Integer pageNo = Optional.ofNullable(param.getInteger("pageNo")).orElse(1);
        if (pageNo == -1) {
            List var14 = dataService.dataList(code, param);
            if (var14 != null && var14.size() != 0) {
                total = var14.size();
                records = var14;
            }
        } else {
            IPage var16 = dataService.dataPage(code, param);
            total = var16.getTotal();
            records = var16.getRecords();
        }

        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>().eq(OnlTableField::getHeadId, code));
        HashMap<String, Object> var5 = new HashMap<>();
        var5.put("primaryKey", fields.stream().filter(a -> CommonConstant.IS_TRUE.equals(a.getDbIsKey()))
                .findFirst().map(OnlTableField::getDbFieldName).get());
        var5.put("total", total);
        var5.put("records", records);
        return var5;
    }

    @ApiOperation(value = "获取单条数据、根据父级查获取单条数据", notes = "获取单条数据")
    @GetMapping({"/data/{code}/{dataId}", "/data/{code}/{dataId}/{parentCode}"})
    public Map<String, Object> data(@PathVariable("code") String code, @PathVariable("dataId") String dataId,
                                    @PathVariable(value = "parentCode", required = false) String parentCode,
                                    @RequestParam(value = "subCodes", required = false) String[] subCodes) throws Exception {
        return dataService.queryDBTableData(code, dataId,
                Optional.ofNullable(parentCode).filter(i -> !code.equals(i)).orElse(null), subCodes);
    }

    @ApiOperation(value = "表数据添加", notes = "表数据添加")
    @PostMapping({"/data/{code}"})
    public Boolean add(@PathVariable("code") String code, @RequestBody JSONObject param) throws Exception {
        dataService.saveDBTableData(code, null, new JSONArray(Collections.singletonList(param)));
        return true;
    }

    @ApiOperation(value = "根据父级表数据添加", notes = "根据父级表数据添加")
    @PostMapping({"/data/{code}/{parentId}/{parentCode}"})
    public Boolean add(@PathVariable("code") String code,
                       @PathVariable("parentId") String parentId, @PathVariable("parentCode") String parentCode,
                       @RequestBody JSONObject param) throws Exception {
        JSONObject fkMap = new JSONObject();
        if (!code.equals(parentCode)) {
            fkMap.put(Optional.of(headService.getById(parentCode)).get().getTableNameOld(), parentId);
        }
        dataService.saveDBTableData(code, fkMap, new JSONArray(Collections.singletonList(param)));
        return true;
    }

    @ApiOperation(value = "表数据修改", notes = "表数据修改")
    @PutMapping({"/data/{code}", "/data/{code}/{pkId}"})
    public Boolean edit(@PathVariable("code") String code, @PathVariable(value = "pkId", required = false) String pkId,
                        @RequestBody JSONObject param) throws Exception {
        if (StringUtils.isNotBlank(pkId) && !param.values().contains(pkId)) {
            throw new NullPointerException("主键值不存在！！！");
        }
        dataService.editDBTableData(code, null, new JSONArray(Collections.singletonList(param)));
        return true;
    }

    @ApiOperation(value = "表数据修改", notes = "表数据修改")
    @PutMapping({"/data/{code}/{parentId}/{parentCode}"})
    public Boolean edit(@PathVariable("code") String code,
                        @PathVariable("parentId") String parentId, @PathVariable("parentCode") String parentCode,
                        @RequestBody JSONObject param) throws Exception {
        JSONObject fkMap = new JSONObject();
        if (!code.equals(parentCode)) {
            fkMap.put(Optional.of(headService.getById(parentCode)).get().getTableNameOld(), parentId);
        }
        dataService.editDBTableData(code, fkMap, new JSONArray(Collections.singletonList(param)));
        return true;
    }

    @ApiOperation(value = "表数据删除", notes = "表数据删除")
    @DeleteMapping({"/data/{code}/{id}"})
    public Boolean e(@PathVariable("code") String code, @PathVariable("id") String var2) throws Exception {
        return dataService.deleteDBTableData(code, var2.split(",")) > 0;
    }

    @ApiOperation(value = "表数据导出", notes = "表数据导出")
    @PostMapping({"/data/export/{code}"})
    public void exportXls(@PathVariable("code") String code, @RequestBody JSONObject param, HttpServletResponse response) throws IOException {

        List<Map<String, Object>> records = new ArrayList<>();
        Integer pageNo = Optional.ofNullable(param.getInteger("pageNo")).orElse(1);
        if (pageNo == -1) {
            List var14 = dataService.dataList(code, param);
            if (var14 != null && var14.size() != 0) {
                records = var14;
            }
        } else {
            IPage var16 = dataService.dataPage(code, param);
            records = var16.getRecords();
        }

        OnlTableHead head = headService.requireOne(code);
        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, code).eq(OnlTableField::getIsShowList, CommonConstant.IS_TRUE)
                .orderByAsc(OnlTableField::getOrderNum));

        Map<String, ExcelField> map = fields.stream()
                .collect(Collectors.toMap(OnlTableField::getDbFieldNameOld, v -> new ExcelFieldImpl(v.getDbFieldTxt()).setAlign(2).setSort(v.getOrderNum())));

        String fileName = head.getTableTxt() + "_data.xlsx";
        ExportExcel export = new ExportExcel("", map);
        export.setDataList(records);
        export.write(response, fileName);
//        export.writeFile("F:\\data\\" + fileName);
    }

    @ApiOperation(value = "表数据导入", notes = "表数据导入")
    @PostMapping({"/data/import/{code}"})
    public Boolean importXls(@PathVariable("code") String code, HttpServletRequest request) throws Exception {
        return true;
    }

}
