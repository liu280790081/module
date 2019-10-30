package orange.onl_table.onl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.common.config.DataBaseConfig;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.engine.FreemarkerHelper;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.common.util.ConvertUtils;
import orange.onl_table.common.util.UUIDGenerator;
import orange.onl_table.db.TableConfigModel;
import orange.onl_table.db.service.IDbHandler;
import orange.onl_table.db.tool.BTool;
import orange.onl_table.onl.dao.OnlTableFieldMapper;
import orange.onl_table.onl.dao.OnlTableHeadMapper;
import orange.onl_table.onl.dto.DbColumn;
import orange.onl_table.onl.dto.DbTable;
import orange.onl_table.onl.dto.TableModel;
import orange.onl_table.onl.entity.OnlTableField;
import orange.onl_table.onl.entity.OnlTableHead;
import orange.onl_table.onl.entity.OnlTableIndex;
import orange.onl_table.onl.service.OnlTableFieldService;
import orange.onl_table.onl.service.OnlTableHeadService;
import orange.onl_table.onl.service.OnlTableIndexService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class OnlTableHeadServiceImpl extends ServiceImpl<OnlTableHeadMapper, OnlTableHead> implements OnlTableHeadService {

    @Autowired
    private DataBaseConfig dbConfig;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private OnlTableFieldService fieldService;
    @Autowired
    private OnlTableIndexService indexService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean addAll(TableModel model) {
        String headId = UUIDGenerator.generate();
        OnlTableHead head = model.getHead().newInsert(headId);

        int var6 = 0;
        List<OnlTableField> fields = model.getFields();
        for (OnlTableField var7 : fields) {
            var7.newInsert(UUIDGenerator.generate(), headId, ++var6);
        }

        List<OnlTableIndex> indices = model.getIndexs();
        for (OnlTableIndex var9 : indices) {
            var9.newInsert(UUIDGenerator.generate(), headId);
        }

        super.save(head);
        fieldService.saveBatch(fields);
        indexService.saveBatch(indices);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean editAll(TableModel model) {
        OnlTableHead head = super.getById(model.getHead().getId()).newUpdate(model.getHead());

        /* 表字段更新 */
        List<OnlTableField> oldFields = fieldService.list(new LambdaQueryWrapper<OnlTableField>().eq(OnlTableField::getHeadId, head.getId()));
        List<OnlTableField> addFields = new ArrayList<>(), updateFields = new ArrayList<>();
        for (OnlTableField newField : Optional.ofNullable(model.getFields()).orElse(new ArrayList<>())) {
            int idx = oldFields.indexOf(new OnlTableField(newField.getId(), newField.getDbFieldName()));
            if (idx == -1) {
                addFields.add(new OnlTableField().newInsert(newField, UUIDGenerator.generate(), head));
            } else {
                updateFields.add(oldFields.remove(idx).newUpdate(newField, head));
            }
        }

        /* 表索引更新 */
        List<OnlTableIndex> oldIndexes = indexService.list(new LambdaQueryWrapper<OnlTableIndex>().eq(OnlTableIndex::getHeadId, head.getId()));
        List<OnlTableIndex> addIndexes = new ArrayList<>(), updateIndexes = new ArrayList<>();
        for (OnlTableIndex newIndex : Optional.ofNullable(model.getIndexs()).orElse(new ArrayList<>())) {
            int idx = oldIndexes.indexOf(new OnlTableIndex(newIndex));
            if (idx == -1) {
                addIndexes.add(new OnlTableIndex().newInsert(newIndex, UUIDGenerator.generate(), head.getId()));
            } else {
                updateIndexes.add(oldIndexes.remove(idx).newUpdate(newIndex, head));
            }
        }

        if (!addIndexes.isEmpty()) {
            indexService.saveBatch(addIndexes);
        }
        if (!updateIndexes.isEmpty()) {
            indexService.updateBatchById(updateIndexes);
        }
        if (!oldIndexes.isEmpty()) {
            head.setIsDbSync(CommonConstant.IS_FALSE_STR);
            indexService.removeByIds(oldIndexes);
        }

        if (!addFields.isEmpty()) {
            fieldService.saveBatch(addFields);
        }
        if (!updateFields.isEmpty()) {
            fieldService.updateBatchById(updateFields);
        }
        if (!oldFields.isEmpty()) {
            head.setIsDbSync(CommonConstant.IS_FALSE_STR);
            fieldService.removeByIds(oldFields);
        }

        super.updateById(head);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void doDbSync(String code, String synMethod) throws Exception {
        OnlTableHead head = Optional.of(super.getById(code)).get();

        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, code)
                .orderByDesc(OnlTableField::getDbIsKey));

        switch (synMethod) {
            case "normal":
                boolean isExist = this.checkDBTableExist(head.getTableNameOld());
                if (isExist) {
                    IDbHandler handler = IDbHandler.dbHandler(dbConfig.getDbName());
                    List<String> sqlList = new ArrayList<>();

                    // 清空索引
                    List<OnlTableIndex> indices = indexService.list((new LambdaQueryWrapper<OnlTableIndex>()).eq(OnlTableIndex::getHeadId, code));
                    for (OnlTableIndex index : indices) {
                        sqlList.add(handler.indexDropSql(head.getTableNameOld(), index.getIndexName()));
                    }

                    // 更新表sql
                    sqlList.add(handler.tableUpdateSql(new DbTable().newInsert(head)));

                    // 字段更新
                    List<DbColumn> dbColumns = baseMapper.getDBTableColumnList(dbConfig.getSchema(), head.getTableNameOld());
                    sqlList.addAll(fields.stream()
                            .collect(Collectors.toMap(f -> f, f -> {
                                int index = dbColumns.indexOf(new DbColumn(f.getDbFieldNameOld()));
                                if (index > -1) {
                                    return dbColumns.remove(index);
                                }
                                return new DbColumn("-1");
                            })).entrySet()
                            .stream()
                            .map(e -> ("-1".equals(e.getValue().getColumnName())) ?
                                    handler.columnAddSql(head.getTableName(), new DbColumn().newInsert(e.getKey()))
                                    : handler.columnUpdateSql(head.getTableName(), new DbColumn().newInsert(e.getKey())))
                            .collect(Collectors.toList()));

                    for (DbColumn column : dbColumns) {
                        sqlList.add(handler.columnDropSql(head.getTableName(), column.getColumnName()));
                    }

                    for (String sql : sqlList) {
                        baseMapper.executeDDL(sql);
                    }
                } else {
                    this.createDBTable(new TableConfigModel().init(dbConfig, head, fields));
                }
                break;
            case "force":
                baseMapper.dropDBTable(dbConfig.getSchema(), head.getTableName());
                this.createDBTable(new TableConfigModel().init(dbConfig, head, fields));
                break;
        }


        updateById(Stream.of(head).peek(h -> {
            h.setIsDbSync(CommonConstant.IS_TRUE_STR);
            h.setTableNameOld(head.getTableName());
        }).findFirst().get());

        fieldService.updateBatchById(fields.stream().peek(f -> {
            f.setDbFieldNameOld(f.getDbFieldName());
            f.setDbSync(CommonConstant.IS_TRUE);
        }).collect(Collectors.toList()));
        indexService.createIndex(code, dbConfig.getDbName(), head.getTableName());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteRecordAndTable(List<String> ids, boolean isDelete) throws OnlException {
        List<OnlTableHead> var3 = baseMapper.selectBatchIds(ids);
        List<String> list = ids.stream().filter(k -> var3.indexOf(new OnlTableHead(k)) == -1).collect(Collectors.toList());
        if (list.size() > 0) {
            throw new OnlException(list + "实体配置不存在");
        }
        if (isDelete) {
            List<OnlTableHead> needDrop = var3.stream()
                    .filter(a -> baseMapper.checkDBTableExist(dbConfig.getSchema(), a.getTableName()))
                    .collect(Collectors.toList());
            for (OnlTableHead head : needDrop) {
                String sql = IDbHandler.dbHandler(dbConfig.getDbName()).tableDropSql(head.getTableName());
                baseMapper.executeDDL(sql);
            }
        }
        fieldService.remove(new LambdaQueryWrapper<OnlTableField>().in(OnlTableField::getHeadId, ids));
        indexService.remove(new LambdaQueryWrapper<OnlTableIndex>().in(OnlTableIndex::getHeadId, ids));
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveBatchDbTable2Online(String... tableNames) throws Exception {
        Objects.requireNonNull(tableNames);

        LambdaQueryWrapper<OnlTableHead> wrapper = new LambdaQueryWrapper<OnlTableHead>().in(OnlTableHead::getTableName, tableNames);
        List<String> list = super.list(wrapper).stream().map(OnlTableHead::getTableName).collect(Collectors.toList());

        if (list.size() > 0) {
            throw new OnlException("表" + list + "存在");
        }

        for (String aVar4 : tableNames) {
            if (ConvertUtils.isNotEmpty(aVar4)) {
                int var6 = this.count((new LambdaQueryWrapper<OnlTableHead>()).eq(OnlTableHead::getTableName, aVar4));
                if (var6 <= 0) {
                    log.info("[IP] [online数据库导入表]   --表名：" + aVar4);
                    this.saveDbTable2Online(aVar4);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveDbTable2Online(String tableName) {
        DbTable table = this.queryDBTableInfo(tableName);
        if (table == null) {
            throw new NullPointerException("表名 : " + tableName + "，获取信息为空");
        }
        String headId = UUIDGenerator.generate();
        OnlTableHead var2 = new OnlTableHead().newInsert(table, headId);

        int var6 = 0;
        List<OnlTableField> var4 = new ArrayList<>();
        for (DbColumn var7 : table.getColumnVos()) {
            var4.add(new OnlTableField().newInsert(var7, headId, UUIDGenerator.generate(), ++var6));
        }
        save(var2);
        fieldService.saveBatch(var4);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveBatchOnlineTable(String tableName, List<OnlTableField> fieldList, List<Map<String, Object>> dataList) {

        try (SqlSession var4 = this.sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)) {
            OnlTableFieldMapper var5 = var4.getMapper(OnlTableFieldMapper.class);
            short var6 = 1000;
            int var7;
            String var8;
            Map var9;
            if (var6 >= dataList.size()) {
                for (var7 = 0; var7 < dataList.size(); ++var7) {
                    var8 = JSON.toJSONString(dataList.get(var7));
                    var9 = BTool.assembleInsertSql(tableName, fieldList, JSONObject.parseObject(var8));
                    var5.executeInsertSQL(var9);
                }
            } else {
                for (var7 = 0; var7 < dataList.size(); ++var7) {
                    var8 = JSON.toJSONString(dataList.get(var7));
                    var9 = BTool.assembleInsertSql(tableName, fieldList, JSONObject.parseObject(var8));
                    var5.executeInsertSQL(var9);
                    if (var7 % var6 == 0) {
                        var4.commit();
                        var4.clearCache();
                    }
                }
            }

            var4.commit();
        } catch (Exception var13) {
            var13.printStackTrace();
        }

    }

    @Override
    public List<Map<String, Object>> queryListData(String sql) {
        return baseMapper.queryList(sql);
    }

    @Override
    public List<OnlTableHead> queryContainFKScheduleList(String pkTableId) {
        return baseMapper.getContainFKScheduleList(pkTableId);
    }

    /* ----------------------------------------数据库实体表------------------------------------------------ */

    @Override
    public void createDBTable(TableConfigModel config) throws Exception {
        HashMap<String, Object> template = new HashMap<>();
        template.put("entity", config);
        template.put("dataType", dbConfig.getDbName());
        String var2 = FreemarkerHelper.generateByUTF8(FreemarkerHelper.TABLE_TEMPLATE, template);
        log.info(var2);

        DataBaseConfig var4 = config.getDbConfig();
        HashMap<String, java.io.Serializable> var3 = new HashMap<>();
        var3.put("hibernate.connection.driver_class", var4.getDriverClassName());
        var3.put("hibernate.connection.url", var4.getUrl());
        var3.put("hibernate.connection.username", var4.getUsername());
        var3.put("hibernate.connection.password", var4.getPassword());
        var3.put("hibernate.show_sql", true);
        var3.put("hibernate.format_sql", true);
        var3.put("hibernate.dialect", IDbHandler.dialect(dbConfig.getDbName()));
        var3.put("hibernate.hbm2ddl.auto", "create");
        var3.put("hibernate.connection.autocommit", false);
        var3.put("hibernate.current_session_context_class", "thread");
        StandardServiceRegistry var5 = (new StandardServiceRegistryBuilder()).applySettings(var3).build();
        MetadataSources var6 = new MetadataSources(var5);
        ByteArrayInputStream var7 = new ByteArrayInputStream(var2.getBytes());
        var6.addInputStream(var7);
        Metadata var8 = var6.buildMetadata();
        SchemaExport var9 = new SchemaExport();
        var9.create(EnumSet.of(TargetType.DATABASE), var8);
        var7.close();
    }

    private final static String[] STARTS_WITH_TABLE_STR = new String[]{"sys", "onl", "crawler"};

    @Override
    public List<Map<String, String>> queryDBTableList(String keyword) {
        return baseMapper.getDBTableList(dbConfig.getSchema(), keyword).stream()
                .filter(i -> !StringUtils.startsWithAny(i.get("table_name"), STARTS_WITH_TABLE_STR))
                .map(m -> {
                    Map<String, String> map = new HashMap<>();
                    m.forEach((k, v) -> map.put(ConvertUtils.camelName(k), v));
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> queryDBTableMap(String keyword) {
        return baseMapper.getDBTableList(dbConfig.getSchema(), keyword).stream()
                .filter(i -> !StringUtils.startsWithAny(i.get("table_name"), STARTS_WITH_TABLE_STR))
                .collect(Collectors.toMap(i -> i.get("table_name"), i -> i.get("table_comment")));
    }

    @Override
    public DbTable queryDBTableInfo(String tableName) {
        DbTable table = baseMapper.getDBTableInfo(dbConfig.getSchema(), tableName);
        table.setColumnVos(baseMapper.getDBTableColumnList(dbConfig.getSchema(), tableName));
        return table;
    }

    @Override
    public boolean checkDBTableExist(String tableName) {
        return baseMapper.checkDBTableExist(dbConfig.getSchema(), tableName);
    }

    /* ----------------------------数据库实体表数据-------------------------------------- */

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String saveDBTableData(String code, JSONObject data) throws OnlException, Exception {
        OnlTableHead var3 = getById(code);
        if (var3 == null) {
            throw new OnlException("数据库主表ID[" + code + "]不存在");
        }
        String pk = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, code)
                .eq(OnlTableField::getDbIsKey, CommonConstant.IS_TRUE))
                .stream().findFirst().get().getDbFieldName();
        final String id = UUIDGenerator.generate();
        data.put(pk, id);
        if (CommonConstant.IS_TRUE_STR.equals(var3.getIsTree())) {
            fieldService.saveTreeFormData(code, var3.getTableName(), data, var3.getTreeIdField(), var3.getTreeParentIdField());
        } else {
            fieldService.saveFormData(code, var3.getTableName(), data);
        }
        return id;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveDBTableData(String headId, JSONObject fkJson, JSONArray dataAry) throws Exception {
        OnlTableHead head = Optional.of(getById(headId)).get();
        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, head.getId()));
        String pk = fields.stream().filter(f -> CommonConstant.IS_TRUE.equals(f.getDbIsKey()))
                .findFirst().map(OnlTableField::getDbFieldNameOld).get();

        for (JSONObject data : dataAry.toJavaList(JSONObject.class)) {
            final String pkVal = UUIDGenerator.generate();
            data.put(pk, pkVal);
            if (!Objects.isNull(fkJson) && fkJson.size() > 0) {
                for (Map.Entry<String, Object> fk : fkJson.entrySet()) {
                    fields.stream().filter(f -> Objects.equals(fk.getKey(), f.getMainTable()))
                            .forEach(f -> data.put(f.getDbFieldNameOld(), fk.getValue()));
                }
            }

            if (CommonConstant.IS_TRUE_STR.equals(head.getIsTree())) {
                fieldService.saveTreeFormData(head, fields, data);
            } else {
                fieldService.saveFormData(head, fields, data);
            }

            if (data.containsKey("subList")) {
                for (Map.Entry<String, Object> subData : data.getJSONObject("subList").entrySet()) {
                    JSONObject fkMap = new JSONObject();
                    fkMap.put(head.getTableNameOld(), pkVal);
                    this.saveDBTableData(subData.getKey(), fkMap, (JSONArray) JSON.toJSON(subData.getValue()));
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void editDBTableData(String code, JSONObject json) throws Exception {
        OnlTableHead var3 = Optional.of(getById(code)).get();

        String var5 = var3.getTableName();
        if (Objects.equals(CommonConstant.IS_TRUE_STR, var3.getIsTree())) {
            fieldService.editTreeFormData(code, var5, json, var3.getTreeIdField(), var3.getTreeParentIdField());
        } else {
            fieldService.editFormData(code, var5, json);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void editDBTableData(String headId, JSONObject fkJson, JSONArray dataAry) throws Exception {
        OnlTableHead head = Optional.of(getById(headId)).get();
        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, head.getId()));

        String pk = fields.stream().filter(f -> CommonConstant.IS_TRUE.equals(f.getDbIsKey()))
                .findFirst().map(OnlTableField::getDbFieldNameOld).get();

        final boolean isHaveFK = fkJson != null && !fkJson.isEmpty();

        List<Map<String, Object>> oldList = null;
        if (isHaveFK) {
            fkJson = new JSONObject(fkJson.entrySet().stream()
                    .collect(Collectors.toMap(k -> fields.stream()
                            .filter(f -> Objects.equals(k.getKey(), f.getMainTable()))
                            .map(OnlTableField::getDbFieldNameOld)
                            .findFirst().get(), Map.Entry::getValue)));
            oldList = baseMapper.getDBTableDataList(dbConfig.getSchema(), head.getTableName(), fkJson);
        }

        for (JSONObject data : dataAry.toJavaList(JSONObject.class)) {
            boolean isAdd = StringUtils.isBlank(data.getString(pk));
            String pkVal = isAdd ? UUIDGenerator.generate() : data.getString(pk);

            // 判断增、改
            if (isAdd) {
                data.put(pk, pkVal);
                if (isHaveFK) {
                    data.putAll(fkJson);
                }
                if (CommonConstant.IS_TRUE_STR.equals(head.getIsTree())) {
                    fieldService.saveTreeFormData(head, fields, data);
                } else {
                    fieldService.saveFormData(head, fields, data);
                }
            } else {
                if (isHaveFK && !oldList.isEmpty()) {
                    oldList.removeIf(old -> Objects.equals(pkVal, old.get(pk)));
                }
                if (CommonConstant.IS_TRUE_STR.equals(head.getIsTree())) {
                    fieldService.editTreeFormData(head, fields, pk, data);
                } else {
                    fieldService.editFormData(head, fields, data);
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
            Map<String, Object> pkParam = new HashMap<>();
            pkParam.put(pk, pkVals);
            baseMapper.deleteDBTableData(dbConfig.getSchema(), head.getTableName(), pkParam);
        }
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int deleteDBTableData(String headId, String... dataIds) throws Exception {
        Objects.requireNonNull(headId);
        Objects.requireNonNull(dataIds);
        OnlTableHead head = Optional.of(getById(headId)).get();

        Map<String, Object> pkParam = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, headId)
                .eq(OnlTableField::getDbIsKey, CommonConstant.IS_TRUE))
                .stream().collect(Collectors.toMap(OnlTableField::getDbFieldName, f -> dataIds));

        if (baseMapper.getDBTableExist(dbConfig.getSchema(), head.getTableName(), pkParam) == 0) {
            throw new OnlException("表数据不存在");
        }

        return baseMapper.deleteDBTableData(dbConfig.getSchema(), head.getTableName(), pkParam);
    }

    @Override
    public Map<String, Object> queryDBTableData(String headId, String id) throws OnlException {
        OnlTableHead head = Optional.of(getById(headId)).get();

        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, headId));
        Map<String, Object> pkParam = fields.stream().filter(f -> CommonConstant.IS_TRUE.equals(f.getDbIsKey()))
                .collect(Collectors.toMap(OnlTableField::getDbFieldName, f -> id));

        Map<String, Object> data = baseMapper.getDBTableData(dbConfig.getSchema(), head.getTableNameOld(), pkParam)
                .entrySet().stream().collect(Collectors.toMap(e -> StringUtils.lowerCase(e.getKey()), e -> {
                    if (e.getValue().getClass().isAssignableFrom(Timestamp.class)) {
                        return DateFormatUtils.format((Timestamp) e.getValue(), "yyyy-MM-dd HH:mm:ss");
                    }
                    return e.getValue();
                }));

        return data;
    }

    @Override
    public Map<String, Object> queryDBTableData(String headId, String pkId, String... subHeadIds) throws Exception {
        OnlTableHead head = Optional.of(getById(headId)).get();

        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, headId));

        Map<String, Object> param = fields.stream().filter(f -> CommonConstant.IS_TRUE.equals(f.getDbIsKey()))
                .collect(Collectors.toMap(OnlTableField::getDbFieldName, f -> pkId));

        Map<String, Object> data = baseMapper.getDBTableData(dbConfig.getSchema(), head.getTableNameOld(), param)
                .entrySet().stream().collect(Collectors.toMap(e -> StringUtils.lowerCase(e.getKey()), e -> {
                    if (e.getValue().getClass().isAssignableFrom(Timestamp.class)) {
                        return DateFormatUtils.format((Timestamp) e.getValue(), "yyyy-MM-dd HH:mm:ss");
                    }
                    return e.getValue();
                }));

        if (ArrayUtils.isNotEmpty(subHeadIds)) {
            Map<String, Object> subDataMap = new HashMap<>();
            List<OnlTableHead> subHeads = baseMapper.selectBatchIds(Arrays.asList(subHeadIds));
            for (OnlTableHead subHead : subHeads) {
                Map<String, Object> fkJson = fieldService.list(new LambdaQueryWrapper<OnlTableField>().eq(OnlTableField::getHeadId, subHead.getId()))
                        .stream().filter(f -> Objects.equals(head.getTableNameOld(), f.getMainTable()))
                        .collect(Collectors.toMap(OnlTableField::getDbFieldNameOld, v -> pkId));
                if (MapUtils.isEmpty(fkJson)) {
                    throw new OnlException("表" + subHead.getTableNameOld() + "与主表没有依赖关系！！！");
                }
                subDataMap.put(subHead.getId(), baseMapper.getDBTableDataList(dbConfig.getSchema(), subHead.getTableNameOld(), fkJson));
            }
            data.put("subList", subDataMap);
        }
        return data;
    }

    @Override
    public Map<String, Object> queryDBTableAuto(String headId, JSONObject params) throws Exception {
        OnlTableHead head = Optional.ofNullable(super.getById(headId))
                .orElseThrow(() -> new OnlException("实体不存在"));
//
//        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
//                .eq(OnlTableField::getHeadId, headId).orderByDesc(OnlTableField::getDbIsKey));
//
//
//        Map<String, Object> result = new HashMap<>();
////        result.put("total", total);
////        result.put("records", records);
//        result.put("primaryKey", fields.stream()
//                .filter(a -> CommonConstant.IS_TRUE.equals(a.getDbIsKey()))
//                .findFirst().map(OnlTableField::getDbFieldName).get());

        LambdaQueryWrapper<OnlTableField> var6 = new LambdaQueryWrapper<>();
        var6.eq(OnlTableField::getHeadId, headId).orderByDesc(OnlTableField::getDbIsKey);
        List<OnlTableField> var8 = fieldService.list(var6);
        String primaryKey = var8.stream().filter(a -> CommonConstant.IS_TRUE.equals(a.getDbIsKey()))
                .findFirst().map(OnlTableField::getDbFieldName).get();
        StringBuffer var9 = new StringBuffer();
        BTool.a(head.getTableName(), var8, var9);
        String var10 = BTool.a(var8, params);
        var9.append(" where 1=1  ").append(var10);
        String var11 = Optional.ofNullable(params.getString("order")).orElse("");
        String var12 = Optional.ofNullable(params.getString("column")).orElse("");

        boolean var3 = false;
        for (OnlTableField var5 : var8) {
            if (ConvertUtils.camelToUnderline(var12).equals(var5.getDbFieldName())) {
                var3 = true;
                break;
            }
        }

        if (var3) {
            var9.append(" ORDER BY ").append(ConvertUtils.camelToUnderline(var12));
            if ("asc".equals(var11)) {
                var9.append(" asc");
            } else {
                var9.append(" desc");
            }
        }

        long total = 0;
        List<Map<String, Object>> records = new ArrayList<>();
//        Integer var13 = params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString());
//        if (var13 == -521) {
//            List var14 = fieldService.queryListBySql(var9.toString());
//            if (var14 != null && var14.size() != 0) {
//                total = var14.size();
//                records = var14;
//            }
//        } else {
//            Integer var17 = params.get("pageNo") == null ? 1 : Integer.parseInt(params.get("pageNo").toString());
//            log.info("---Online查询sql:>>" + var9.toString());
//            IPage var16 = fieldService.selectPageBySql(new Page(var17, var13), var9.toString());
//            total = var16.getTotal();
//            records = var16.getRecords();
//        }

        HashMap<String, Object> var5 = new HashMap<>();
        var5.put("primaryKey", primaryKey);
        var5.put("total", total);
        var5.put("records", records);
        return var5;
    }
}

