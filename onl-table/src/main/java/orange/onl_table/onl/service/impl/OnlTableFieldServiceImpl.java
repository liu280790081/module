package orange.onl_table.onl.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.util.ConvertUtils;
import orange.onl_table.db.tool.BTool;
import orange.onl_table.onl.dao.OnlTableFieldMapper;
import orange.onl_table.onl.entity.OnlTableField;
import orange.onl_table.onl.entity.OnlTableHead;
import orange.onl_table.onl.service.OnlTableFieldService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OnlTableFieldServiceImpl extends ServiceImpl<OnlTableFieldMapper, OnlTableField> implements OnlTableFieldService {

//    @Autowired
//    private DictItemService dictItemService;

    @Override
    public Map<String, Object> queryAuto(String tbName, String headId, Map<String, Object> params, List<String> needList) {
        LambdaQueryWrapper<OnlTableField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OnlTableField::getHeadId, headId).orderByDesc(OnlTableField::getDbIsKey);
        List<OnlTableField> fields = super.list(wrapper);

        // 主键
        String primaryKey = fields.stream().filter(a -> CommonConstant.IS_TRUE.equals(a.getDbIsKey()))
                .findFirst().map(OnlTableField::getDbFieldName).get();

        // 字典字段
        Map<String, OnlTableField> dictFieldMap = fields.stream()
                .filter(f -> StringUtils.isNotBlank(f.getDictField()))
                .collect(Collectors.toMap(OnlTableField::getDbFieldName, f -> f));

        StringBuffer sql = new StringBuffer();
        BTool.a(tbName, fields, sql);
        String var10 = BTool.a(fields, params);
        sql.append(" where 1=1  ").append(var10);
        String var11 = Optional.ofNullable(params.get("order")).map(String::valueOf).orElse("");
        String var12 = Optional.ofNullable(params.get("column")).map(String::valueOf).orElse("");
        if (a(var12, fields)) {
            sql.append(" ORDER BY " + ConvertUtils.camelToUnderline(var12));
            if ("asc".equals(var11)) {
                sql.append(" asc");
            } else {
                sql.append(" desc");
            }
        }

        long total = 0;
        List<Map<String, Object>> records = new ArrayList<>();
        Integer var13 = params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString());
        if (var13 == -521) {
            List var14 = baseMapper.queryListBySql(sql.toString());
            if (var14 != null && var14.size() != 0) {
                total = var14.size();
                records = var14;
            }
        } else {
            Integer var17 = params.get("pageNo") == null ? 1 : Integer.parseInt(params.get("pageNo").toString());
            log.info("---Online查询sql:>>" + sql.toString());
            IPage var16 = baseMapper.selectPageBySql(new Page(var17, var13), sql.toString());
            total = var16.getTotal();
            records = var16.getRecords();
        }

        HashMap<String, Object> var5 = new HashMap<>();
        var5.put("primaryKey", primaryKey);
        var5.put("total", total);
        var5.put("records", records.stream().map(r -> {
            for (Map.Entry<String, Object> entry : r.entrySet()) {
                if (Objects.isNull(entry.getValue()) || "".equals(entry.getValue())) {
                    continue;
                }
//                if (dictFieldMap.containsKey(entry.getKey())) {
//                    OnlTableField field = dictFieldMap.get(entry.getKey());
//                    DictItem dictItem = dictItemService.findOne(field.getDictField(), String.valueOf(entry.getValue()));
//                    r.put(entry.getKey(), dictItem.getDictValue());
//                }
            }
            return r;
        }).collect(Collectors.toList()));
        return var5;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveFormData(String code, String tbName, JSONObject json) throws Exception {
        LambdaQueryWrapper<OnlTableField> var5 = new LambdaQueryWrapper<>();
        var5.eq(OnlTableField::getHeadId, code);
        List<OnlTableField> var6 = list(var5);
        baseMapper.executeInsertSQL(BTool.assembleInsertSql(tbName, var6, json));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveFormData(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception {
        baseMapper.executeInsertSQL(BTool.assembleInsertSql(head.getTableName(), fields, data));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveTreeFormData(String code, String tbName, JSONObject json, String hasChildField, String pidField) throws Exception {
        LambdaQueryWrapper<OnlTableField> var6 = new LambdaQueryWrapper<>();
        var6.eq(OnlTableField::getHeadId, code);
        List<OnlTableField> var7 = list(var6);

        for (OnlTableField var9 : var7) {
            if (hasChildField.equals(var9.getDbFieldName()) && var9.getIsShowForm() != 1) {
                var9.setIsShowForm(CommonConstant.IS_TRUE);
                json.put(hasChildField, "0");
            } else if (pidField.equals(var9.getDbFieldName()) && ConvertUtils.isEmpty(json.get(pidField))) {
                var9.setIsShowForm(CommonConstant.IS_TRUE);
                json.put(pidField, "0");
            }
        }

        Map var10 = BTool.assembleInsertSql(tbName, var7, json);
        baseMapper.executeInsertSQL(var10);
        if (!"0".equals(json.getString(pidField))) {
            baseMapper.editFormData("update " + tbName + " set " + hasChildField + " = '1' where id = '" + json.getString(pidField) + "'");
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveTreeFormData(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception {
        String hasChildField = head.getTreeIdField(), pidField = head.getTreeParentIdField();
        for (OnlTableField field : fields) {
            if (hasChildField.equals(field.getDbFieldName()) && field.getIsShowForm() != 1) {
                field.setIsShowForm(CommonConstant.IS_TRUE);
                data.put(hasChildField, CommonConstant.ZERO_SIGN);
            } else if (pidField.equals(field.getDbFieldName())
                    && ConvertUtils.isEmpty(data.get(pidField))) {
                field.setIsShowForm(CommonConstant.IS_TRUE);
                data.put(pidField, CommonConstant.ZERO_SIGN);
            }
        }

        baseMapper.executeInsertSQL(BTool.assembleInsertSql(head.getTableNameOld(), fields, data));
        if (!"0".equals(data.getString(pidField))) {
            String sql = "update " + head.getTableNameOld() + " set " + hasChildField + " = '1' where id = '" + data.getString(pidField) + "'";
            baseMapper.editFormData(sql);
        }
    }

    @Override
    public void editFormData(String code, String tbName, JSONObject json) throws Exception {
        LambdaQueryWrapper<OnlTableField> var5 = new LambdaQueryWrapper<>();
        var5.eq(OnlTableField::getHeadId, code);
        List<OnlTableField> var6 = list(var5);
        baseMapper.executeUpdateSQL(BTool.assembleUpdateSql(tbName, var6, json));
    }

    @Override
    public void editFormData(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception {
        baseMapper.executeUpdateSQL(BTool.assembleUpdateSql(head.getTableNameOld(), fields, data));
    }

    @Override
    public void editTreeFormData(String code, String tbName, JSONObject json, String hasChildField, String pidField) throws Exception {
        String var6 = "select * from " + tbName + " where id = '" + json.getString("id") + "'";
        Map var7 = baseMapper.queryFormData(var6);
        String var8 = var7.get(pidField).toString();
        LambdaQueryWrapper<OnlTableField> var9 = new LambdaQueryWrapper<>();
        var9.eq(OnlTableField::getHeadId, code);
        List<OnlTableField> var10 = list(var9);

        for (OnlTableField var12 : var10) {
            if (pidField.equals(var12.getDbFieldName()) && ConvertUtils.isEmpty(json.get(pidField))) {
                var12.setIsShowForm(1);
                json.put(pidField, "0");
            }
        }

        Map var14 = BTool.assembleUpdateSql(tbName, var10, json);
        baseMapper.executeUpdateSQL(var14);
        if (!var8.equals(json.getString(pidField))) {
            if (!"0".equals(var8)) {
                String var15 = "select count(*) from " + tbName + " where " + pidField + " = '" + var8 + "'";
                Integer var13 = baseMapper.queryCountBySql(var15);
                if (var13 == null || var13 == 0) {
                    baseMapper.editFormData("update " + tbName + " set " + hasChildField + " = '0' where id = '" + var8 + "'");
                }
            }

            if (!"0".equals(json.getString(pidField))) {
                baseMapper.editFormData("update " + tbName + " set " + hasChildField + " = '1' where id = '" + json.getString(pidField) + "'");
            }
        }

    }

    @Override
    public void editTreeFormData(OnlTableHead head, List<OnlTableField> fields, String pk, JSONObject data) throws Exception {
        String tbName = head.getTableNameOld(), pidField = head.getTreeParentIdField(), hasChildField = head.getTreeIdField();

        if (ConvertUtils.isEmpty(data.get(pidField))) {
            data.put(pidField, CommonConstant.ZERO_SIGN);
        }

        baseMapper.executeUpdateSQL(BTool.assembleUpdateSql(tbName, fields, data));

        String oldDataSql = "select * from " + tbName + " where " + pk + " = '" + data.getString(pk) + "'";
        String oldPidVal = Optional.of(baseMapper.queryFormData(oldDataSql)).map(v -> v.get(pidField).toString()).get();
        String newPidVal = data.getString(pidField);
        if (!oldPidVal.equals(newPidVal)) {
            if (!CommonConstant.ZERO_SIGN.equals(oldPidVal)) {
                String var15 = "select count(*) from " + tbName + " where " + pidField + " = '" + oldPidVal + "'";
                Integer var13 = baseMapper.queryCountBySql(var15);
                if (var13 == null || var13 == 0) {
                    baseMapper.editFormData("update " + tbName + " set " + hasChildField + " = '0' where " + pk + " = '" + oldPidVal + "'");
                }
            }

            if (!"0".equals(newPidVal)) {
                baseMapper.editFormData("update " + tbName + " set " + hasChildField + " = '1' where " + pk + " = '" + newPidVal + "'");
            }
        }
    }

    @Override
    public List<Map<String, String>> getAutoListQueryInfo(String code) throws Exception {
        LambdaQueryWrapper<OnlTableField> var2 = new LambdaQueryWrapper<>();
        var2.eq(OnlTableField::getHeadId, code);
        var2.eq(OnlTableField::getIsQuery, 1);
        List var3 = list(var2);
        ArrayList var4 = new ArrayList();
        int var5 = 0;

        HashMap var8;
        for (Iterator var6 = var3.iterator(); var6.hasNext(); var4.add(var8)) {
            OnlTableField var7 = (OnlTableField) var6.next();
            var8 = new HashMap();
            var8.put("label", var7.getDbFieldTxt());
            var8.put("view", var7.getFieldShowType());
            var8.put("mode", var7.getQueryMode());
            var8.put("field", var7.getDbFieldName());
            var8.put("dict", var7.getDictField());
//            if (StringUtils.isNotBlank(var7.getDictField())) {
//                var8.put("dictList", dictItemService.findDictList(new InDictDto(var7.getDictField())));
//            }
            ++var5;
            if (var5 > 2) {
                var8.put("hidden", "1");
            }
        }

        return var4;
    }

    public boolean a(String var1, List<OnlTableField> var2) {
        boolean var3 = false;
        for (OnlTableField var5 : var2) {
            if (ConvertUtils.camelToUnderline(var1).equals(var5.getDbFieldName())) {
                var3 = true;
                break;
            }
        }

        return var3;
    }

}
