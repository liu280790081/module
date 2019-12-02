package orange.onl_table.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.common.config.DataBaseConfig;
import orange.onl_table.common.db.IDataSqlHandler;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.db.tool.DataFormatTool;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.common.util.ConvertUtils;
import orange.onl_table.common.util.IdGenerator;
import orange.onl_table.dao.OnlDataMapper;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;
import orange.onl_table.service.OnlDataService;
import orange.onl_table.service.OnlTableFieldService;
import orange.onl_table.service.OnlTableHeadService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static orange.onl_table.common.constant.CommonConstant.*;

@Slf4j
@Service
public class OnlDataServiceImpl implements OnlDataService {

    @Resource
    private OnlDataMapper baseMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private DataBaseConfig dbConfig;
    @Autowired
    private OnlTableHeadService headService;
    @Autowired
    private OnlTableFieldService fieldService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveDBTableData(String headId, JSONObject fkJson, JSONArray dataAry) throws Exception {
        OnlTableHead head = headService.requireOne(headId);

        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, head.getId()));

        OnlTableField pkField = fields.stream().filter(f -> IS_TRUE.equals(f.getDbIsKey())).findFirst().get();
        String pk = pkField.getDbFieldNameOld();

        for (JSONObject data : dataAry.toJavaList(JSONObject.class)) {
            final String pkVal = idGenerator.next();

            data.put(pk, pkVal);
            if (!Objects.isNull(fkJson) && fkJson.size() > 0) {
                for (Map.Entry<String, Object> fk : fkJson.entrySet()) {
                    fields.stream().filter(f -> Objects.equals(fk.getKey(), f.getMainTable()))
                            .forEach(f -> data.put(f.getDbFieldNameOld(), fk.getValue()));
                }
            }

            if (StringUtils.isNotBlank(head.getTombstoneField())) {
                data.put(head.getTombstoneField(), IS_FALSE);
            }

            this.dataFormat(fields, data);

            if (IS_TRUE_STR.equals(head.getIsTree())) {
                this.dataInsertTree(head, fields, pk, data);
            } else {
                this.dataInsert(head, fields, data);
            }

            if (data.containsKey("subList")) {
                for (Map.Entry<String, Object> subData : data.getJSONObject("subList").entrySet()) {
                    JSONObject fkMap = new JSONObject();
                    fkMap.put(head.getTableNameOld(), pkVal);
                    this.saveDBTableData(subData.getKey(), fkMap, (JSONArray) JSON.toJSON(subData.getValue()));
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void editDBTableData(String headId, JSONObject fkJson, JSONArray dataAry) throws Exception {
        OnlTableHead head = headService.requireOne(headId);
        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, head.getId()));

        OnlTableField pkField = fields.stream().filter(f -> IS_TRUE.equals(f.getDbIsKey())).findFirst().get();
        String pk = pkField.getDbFieldNameOld();

        final boolean isHaveFK = fkJson != null && !fkJson.isEmpty();

        List<Map<String, Object>> oldList = null;
        if (isHaveFK) {
            fkJson = new JSONObject(fkJson.entrySet().stream()
                    .collect(Collectors.toMap(k -> fields.stream()
                            .filter(f -> Objects.equals(k.getKey(), f.getMainTable()))
                            .map(OnlTableField::getDbFieldNameOld)
                            .findFirst()
                            .orElseThrow(() -> new OnlException(head.getTableNameOld() + "表不存表id为: " + k.getKey() + " 的外键")), Map.Entry::getValue)));

            oldList = this.dataList(head, fields, fkJson);
        }

        for (JSONObject data : dataAry.toJavaList(JSONObject.class)) {
            boolean isAdd = StringUtils.isBlank(data.getString(pkField.getDbFieldNameOld()));
            String pkVal = isAdd ? idGenerator.next() : data.getString(pk);

            // 判断增、改
            if (isAdd) {
                data.put(pk, pkVal);
                if (isHaveFK) {
                    data.putAll(fkJson);
                }

                if (StringUtils.isNotBlank(head.getTombstoneField())) {
                    data.put(head.getTombstoneField(), IS_FALSE);
                }

                this.dataFormat(fields, data);

                if (IS_TRUE_STR.equals(head.getIsTree())) {
                    this.dataInsertTree(head, fields, pk, data);
                } else {
                    this.dataInsert(head, fields, data);
                }
            } else {
                if (isHaveFK && !oldList.isEmpty()) {
                    oldList.removeIf(old -> Objects.equals(pkVal, old.get(pk)));
                }

                this.dataFormat(fields, data);

                if (IS_TRUE_STR.equals(head.getIsTree())) {
                    this.dataUpdateTree(head, fields, pk, data);
                } else {
                    this.dataUpdate(head, fields, data);
                }
            }

            if (data.containsKey("subList")) {
                for (Map.Entry<String, Object> subData : data.getJSONObject("subList").entrySet()) {
                    JSONObject fkMap = new JSONObject();
                    fkMap.put(head.getTableNameOld(), pkVal);
                    this.editDBTableData(subData.getKey(), fkMap, (JSONArray) JSON.toJSON(subData.getValue()));
                }
            }
        }

        // 需要删除的数据
        if (oldList != null && !oldList.isEmpty()) {
            List<String> pkVals = oldList.stream().map(v -> v.get(pk).toString()).collect(Collectors.toList());
            JSONObject param = new JSONObject();
            param.put(pk, pkVals);
            this.dataDelete(head, fields, param);
        }
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int deleteDBTableData(String headId, String... dataIds) throws Exception {
        Objects.requireNonNull(headId);
        Objects.requireNonNull(dataIds);
        OnlTableHead head = headService.requireOne(headId);
        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, head.getId()));

        JSONObject param = new JSONObject(fields.stream()
                .filter(f -> Objects.equals(IS_TRUE, f.getDbIsKey()))
                .collect(Collectors.toMap(OnlTableField::getDbFieldName, f -> dataIds)));

        return this.dataDelete(head, fields, param);
    }

    @Override
    public List<Map<String, Object>> queryDBTableData(String headId, JSONObject fkJson, JSONArray dataAry) throws Exception {
        OnlTableHead head = headService.requireOne(headId);

        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, headId));

//        for (JSONObject data : dataAry.toJavaList(JSONObject.class)) {
//
//        }

        return null;
    }

    @Override
    public Map<String, Object> queryDBTableData(String headId, String dataId, String parentHeadId, String... subHeadIds) throws Exception {
        OnlTableHead head = headService.requireOne(headId);

        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, headId));

        // 判断是后有上级表id 若有返回表名
        String parentTbName = Optional.ofNullable(parentHeadId)
                .filter(StringUtils::isNotBlank)
                .map(i -> Optional.of(headService.getById(i)).orElseThrow(() -> new OnlException("父级表id不存在")).getTableNameOld())
                .orElse(null);

        Map<String, Object> param = fields.stream()
                .filter(f -> StringUtils.isNotBlank(parentTbName) ? parentTbName.equals(f.getMainTable()) : IS_TRUE.equals(f.getDbIsKey()))
                .collect(Collectors.toMap(OnlTableField::getDbFieldName, f -> dataId));

        if (param.isEmpty()) throw new NullPointerException("参数所对应的字段不存在");

        Map<String, Object> data = Optional.ofNullable(this.dataOne(head, fields, new JSONObject(param)))
                .orElse(new HashMap<>())
                .entrySet().stream().collect(Collectors.toMap(e -> StringUtils.lowerCase(e.getKey()), e -> {
                    if (e.getValue().getClass().isAssignableFrom(Timestamp.class)) {
                        return DateFormatUtils.format((Timestamp) e.getValue(), "yyyy-MM-dd HH:mm:ss");
                    }
                    return e.getValue();
                }));

        if (!data.isEmpty() && ArrayUtils.isNotEmpty(subHeadIds)) {
            String fkId = Optional.ofNullable(parentTbName)
                    .filter(StringUtils::isNotBlank)
                    .map(i -> fields.stream()
                            .filter(f -> IS_TRUE.equals(f.getDbIsKey()))
                            .findFirst()
                            .map(f -> data.get(f.getDbFieldNameOld()).toString())
                            .get())
                    .orElse(dataId);

            Map<String, Object> subDataMap = new HashMap<>();
            List<OnlTableHead> subHeads = new ArrayList<>(headService.listByIds(Arrays.asList(subHeadIds)));
            List<OnlTableField> subFields;
            for (OnlTableHead subHead : subHeads) {
                subFields = fieldService.list(new LambdaQueryWrapper<OnlTableField>().eq(OnlTableField::getHeadId, subHead.getId()));
                Map<String, Object> fkJson = subFields.stream()
                        .filter(f -> Objects.equals(head.getTableNameOld(), f.getMainTable()))
                        .collect(Collectors.toMap(OnlTableField::getDbFieldNameOld, v -> fkId));
                if (MapUtils.isEmpty(fkJson)) {
                    throw new OnlException("表" + subHead.getTableNameOld() + "与主表没有依赖关系！！！");
                }
                subDataMap.put(subHead.getId(), this.dataList(subHead, subFields, new JSONObject(fkJson)));
            }
            data.put("subList", subDataMap);
        }
        return data;
    }

    @Override
    public List<Map<String, Object>> dataList(String headId, JSONObject param) {
        OnlTableHead head = headService.requireOne(headId);

        LambdaQueryWrapper<OnlTableField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OnlTableField::getHeadId, head.getId()).orderByDesc(OnlTableField::getDbIsKey);
        List<OnlTableField> fields = fieldService.list(wrapper);

        return this.dataList(head, fields, param);
    }

    @Override
    public IPage<Map<String, Object>> dataPage(String headId, JSONObject param) {
        OnlTableHead head = headService.requireOne(headId);

        LambdaQueryWrapper<OnlTableField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OnlTableField::getHeadId, head.getId()).orderByDesc(OnlTableField::getDbIsKey);
        List<OnlTableField> fields = fieldService.list(wrapper);

        return this.dataPage(head, fields, param);
    }


    /* -------------------------------- 源操作 ------------------------------------ */

    @Override
    public Map<String, Object> dataOne(OnlTableHead head, List<OnlTableField> fields, JSONObject param) {
        // 判断逻辑删除
        if (StringUtils.isNotBlank(head.getTombstoneField())) {
            param.put(head.getTombstoneField(), IS_FALSE_STR);
        }
        String sql = IDataSqlHandler.dataSqlHandler(dbConfig.getDbName())
                .selectSql(new DbTable().assemble(dbConfig.getSchema(), head), fields, param);
        return baseMapper.executeOneSQL(sql, param);
    }

    @Override
    public Long dataCount(OnlTableHead head, List<OnlTableField> fields, JSONObject param) {
        // 判断逻辑删除
        if (StringUtils.isNotBlank(head.getTombstoneField())) {
            param.put(head.getTombstoneField(), IS_FALSE_STR);
        }
        String sql = IDataSqlHandler.dataSqlHandler(dbConfig.getDbName())
                .countSql(new DbTable().assemble(dbConfig.getSchema(), head), fields, param);
        return baseMapper.executeCountSQL(sql, param);
    }

    @Override
    public List<Map<String, Object>> dataList(OnlTableHead head, List<OnlTableField> fields, JSONObject param) {
        // 判断逻辑删除
        if (StringUtils.isNotBlank(head.getTombstoneField())) {
            param.put(head.getTombstoneField(), IS_FALSE_STR);
        }

        String sql = IDataSqlHandler.dataSqlHandler(dbConfig.getDbName())
                .selectSql(new DbTable().assemble(dbConfig.getSchema(), head), fields, param);
        return baseMapper.executeListSQL(sql, param);
    }

    @Override
    public IPage<Map<String, Object>> dataPage(OnlTableHead head, List<OnlTableField> fields, JSONObject param) {
        // 判断逻辑删除
        if (StringUtils.isNotBlank(head.getTombstoneField())) {
            param.put(head.getTombstoneField(), IS_FALSE_STR);
        }

        String sql = IDataSqlHandler.dataSqlHandler(dbConfig.getDbName())
                .selectSql(new DbTable().assemble(dbConfig.getSchema(), head), fields, param);
        Integer pageNo = Optional.ofNullable(param.getInteger(PAGE_NO)).orElse(1),
                pageSize = Optional.ofNullable(param.getInteger(PAGE_SIZE)).orElse(10);
        return baseMapper.executePageSQL(new Page(pageNo, pageSize), sql, param);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void dataInsert(OnlTableHead head, List<OnlTableField> fields, JSONObject data) {
        String sql = IDataSqlHandler.dataSqlHandler(dbConfig.getDbName())
                .insertSql(new DbTable().assemble(dbConfig.getSchema(), head), fields, data);
        baseMapper.executeInsertSQL(sql, data);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void dataInsertTree(OnlTableHead head, List<OnlTableField> fields, String pk, JSONObject data) {
        String hasChildField = head.getTreeIdField(), pidField = head.getTreeParentIdField();

        for (OnlTableField field : fields) {
            if (hasChildField.equals(field.getDbFieldName()) && field.getIsShowForm() != 1) {
                data.put(hasChildField, ZERO_SIGN);
            } else if (pidField.equals(field.getDbFieldName())
                    && ConvertUtils.isEmpty(data.get(pidField))) {
                data.put(pidField, ZERO_SIGN);
            }
        }

        this.dataInsert(head, fields, data);

        if (!ZERO_SIGN.equals(data.getString(pidField))) {
            ;
            JSONObject parentParam = new JSONObject();
            parentParam.put(hasChildField, IS_TRUE_STR);
            parentParam.put(pk, data.getString(pidField));
            this.dataUpdate(head, fields, parentParam);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void dataUpdate(OnlTableHead head, List<OnlTableField> fields, JSONObject data) {
        String sql = IDataSqlHandler.dataSqlHandler(dbConfig.getDbName())
                .updateSql(new DbTable().assemble(dbConfig.getSchema(), head), fields, data);
        baseMapper.executeUpdateSQL(sql, data);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void dataUpdateTree(OnlTableHead head, List<OnlTableField> fields, String pk, JSONObject data) {
        String pidField = head.getTreeParentIdField(), hasChildField = head.getTreeIdField();

        if (ConvertUtils.isEmpty(data.get(pidField))) {
            data.put(pidField, ZERO_SIGN);
        }

        this.dataUpdate(head, fields, data);

        JSONObject param = new JSONObject();
        param.put(pk, data.getString(pk));
        String oldPidVal = Optional.of(this.dataOne(head, fields, param)).map(v -> v.get(pidField).toString()).get();
        String newPidVal = data.getString(pidField);
        if (!oldPidVal.equals(newPidVal)) {
            if (!ZERO_SIGN.equals(oldPidVal)) {
                param = new JSONObject();
                param.put(pidField, oldPidVal);
                if (this.dataCount(head, fields, param) == 0) {
                    param = new JSONObject();
                    param.put(hasChildField, IS_FALSE_STR);
                    param.put(pk, oldPidVal);
                    this.dataUpdate(head, fields, param);
                }
            }

            if (!"0".equals(newPidVal)) {
                param = new JSONObject();
                param.put(hasChildField, IS_TRUE_STR);
                param.put(pk, newPidVal);
                this.dataUpdate(head, fields, param);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int dataDelete(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception {
        IDataSqlHandler handler = IDataSqlHandler.dataSqlHandler(dbConfig.getDbName());
        DbTable table = new DbTable().assemble(dbConfig.getSchema(), head);

        String countSql = handler.countSql(table, fields, data);
        if (baseMapper.executeCountSQL(countSql, data) == 0) {
            throw new OnlException("表数据不存在");
        }

        if (StringUtils.isNotBlank(head.getTombstoneField())) {
            data.put(head.getTombstoneField(), IS_TRUE_STR);
            String updateSql = handler.updateSql(table, fields, data);
            return baseMapper.executeUpdateSQL(updateSql, data);
        } else {
            String deleteSql = handler.deleteSql(table, fields, data);
            return baseMapper.executeDeleteSQL(deleteSql, data);
        }
    }


    private void dataFormat(List<OnlTableField> fields, JSONObject data) {
        for (OnlTableField field : fields) {
            String fieldName = field.getDbFieldNameOld();
            if (data.containsKey(fieldName)) {
                data.put(fieldName, this.getFormatValue(field, data.get(fieldName)));
            } else {
                if (StringUtils.isNotBlank(field.getDbDefaultVal())) {
                    data.put(fieldName, field.getDbDefaultVal());
                }
                if (Objects.equals(IS_FALSE, field.getDbIsNull())) {
                    throw new OnlException("字段[" + field.getDbFieldTxt() + "]的值不能为空！！！");
                }
            }
        }
    }

    private Object getFormatValue(OnlTableField field, Object value) throws OnlException {
        if (value == null) return null;

        switch (field.getDbType()) {
            case "string":
                return Optional.of(TypeUtils.castToString(value))
                        .filter(v -> {
                            if (field.getDbLength() < v.length()) {
                                throw new OnlException(field.getDbFieldTxt() + "（" + field.getDbLength() + "）字段传入参数超长");
                            }
                            return true;
                        }).get();

            case "int":
            case "decimal":
            case "double":
                return Optional.of(value)
                        .map(v -> "".equals(v) ? 0 : v)
                        .filter(v -> {
                            if (v instanceof String && !NumberUtils.isCreatable(v.toString())) {
                                throw new OnlException("[" + field.getDbFieldTxt() + "] 的参数不是一个数");
                            }
                            return true;
                        })
                        .map(TypeUtils::castToBigDecimal)
                        .filter(v -> {
                            if (field.getDbLength() < v.precision()) {
                                throw new OnlException(field.getDbFieldTxt() + "（" + field.getDbLength() + "）字段传入参数超长");
                            }
                            return true;
                        })
                        .get();
            case "date":
            case "datetime":
                if (!(value instanceof Date)) {
                    if (value instanceof String) {
                        try {
                            String dateStr = String.valueOf(value);
                            if (StringUtils.isBlank(dateStr)) {
                                return null;
                            }
                            return DateUtils.parseDate(dateStr, DataFormatTool.DATE_PARSE_PATTERNS);
                        } catch (ParseException e) {
                            throw new OnlException(field.getDbFieldTxt() + "入参格式不正确！！！");
                        }
                    } else if (value instanceof Long) {
                        Long date = (Long) value;
                        return new Date(date);
                    }
                    throw new OnlException(field.getDbFieldTxt() + "入参格式不正确！！！");
                }
                return value;
            case "text":
            case "longtext":
                if (value instanceof Collection || value.getClass().isArray()) {
                    return JSON.toJSONString(value);
                }
                return value;
        }
        return value;
    }


}
