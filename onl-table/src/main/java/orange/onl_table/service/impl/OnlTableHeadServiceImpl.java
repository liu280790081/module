package orange.onl_table.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.common.config.DataBaseConfig;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.db.IDbHandler;
import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.db.service.DbService;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.common.util.ConvertUtils;
import orange.onl_table.common.util.IdGenerator;
import orange.onl_table.dao.OnlTableHeadMapper;
import orange.onl_table.dto.TableModel;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;
import orange.onl_table.entity.OnlTableIndex;
import orange.onl_table.service.OnlTableFieldService;
import orange.onl_table.service.OnlTableHeadService;
import orange.onl_table.service.OnlTableIndexService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class OnlTableHeadServiceImpl extends ServiceImpl<OnlTableHeadMapper, OnlTableHead> implements OnlTableHeadService {

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private DataBaseConfig dbConfig;
    @Autowired
    private DbService dbService;
    @Autowired
    private OnlTableFieldService fieldService;
    @Autowired
    private OnlTableIndexService indexService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean addAll(TableModel model) {
        String headId = idGenerator.next();
        OnlTableHead head = model.getHead().newInsert(headId);

        AtomicInteger idx = new AtomicInteger();
        List<OnlTableField> fields = model.getFields().stream()
                .peek(f -> f.newInsert(idGenerator.next(), headId, idx.incrementAndGet()))
                .collect(Collectors.toList());

        List<OnlTableIndex> indices = model.getIndices().stream()
                .peek(index -> index.newInsert(idGenerator.next(), headId))
                .collect(Collectors.toList());

        super.save(head);
        fieldService.saveBatch(fields);
        indexService.saveBatch(indices);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean editAll(TableModel model) {
        OnlTableHead head = this.requireOne(model.getHead().getId()).newUpdate(model.getHead());

        /* 表字段更新 */
        List<OnlTableField> oldFields = fieldService.list(new LambdaQueryWrapper<OnlTableField>().eq(OnlTableField::getHeadId, head.getId()));
        List<OnlTableField> addFields = new ArrayList<>(), updateFields = new ArrayList<>();
        for (OnlTableField newField : Optional.ofNullable(model.getFields()).orElse(new ArrayList<>())) {
            int idx = oldFields.indexOf(new OnlTableField(newField.getId(), newField.getDbFieldName()));
            if (idx == -1) {
                addFields.add(new OnlTableField().newInsert(newField, idGenerator.next(), head));
            } else {
                updateFields.add(oldFields.remove(idx).newUpdate(newField, head));
            }
        }

        /* 表索引更新 */
        List<OnlTableIndex> oldIndexes = indexService.list(new LambdaQueryWrapper<OnlTableIndex>().eq(OnlTableIndex::getHeadId, head.getId()));
        List<OnlTableIndex> addIndexes = new ArrayList<>(), updateIndexes = new ArrayList<>();
        for (OnlTableIndex newIndex : Optional.ofNullable(model.getIndices()).orElse(new ArrayList<>())) {
            int idx = oldIndexes.indexOf(new OnlTableIndex(newIndex));
            if (idx == -1) {
                addIndexes.add(new OnlTableIndex().newInsert(newIndex, idGenerator.next(), head.getId()));
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
            indexService.removeByIds(oldIndexes.stream().map(OnlTableIndex::getId).collect(Collectors.toList()));
        }

        if (!addFields.isEmpty()) {
            fieldService.saveBatch(addFields);
        }
        if (!updateFields.isEmpty()) {
            fieldService.updateBatchById(updateFields);
        }
        if (!oldFields.isEmpty()) {
            head.setIsDbSync(CommonConstant.IS_FALSE_STR);
            fieldService.removeByIds(oldFields.stream().map(OnlTableField::getId).collect(Collectors.toList()));
        }

        super.updateById(head);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void doDbSync(String code, String synMethod) throws Exception {
        OnlTableHead head = Optional.ofNullable(baseMapper.selectById(code))
                .orElseThrow(() -> new OnlException("主键为" + code + "不存在!!!"));

        List<OnlTableField> fields = fieldService.list(new LambdaQueryWrapper<OnlTableField>()
                .eq(OnlTableField::getHeadId, code)
                .orderByDesc(OnlTableField::getDbIsKey));

        IDbHandler handler = IDbHandler.dbHandler(dbConfig.getDbName());

        switch (synMethod) {
            case "normal":
                boolean isExist = dbService.checkDBTableExist(head.getTableNameOld());
                if (isExist) {

                    List<String> sqlList = new ArrayList<>();

                    // 清空索引
                    List<OnlTableIndex> indices = indexService.list((new LambdaQueryWrapper<OnlTableIndex>()).eq(OnlTableIndex::getHeadId, code));
                    for (OnlTableIndex index : indices) {
                        sqlList.add(handler.indexDropSql(head.getTableNameOld(), index.getIndexName()));
                    }
                    // 更新表sql
                    sqlList.add(handler.tableUpdateSql(new DbTable().newInsert(head)));

                    // 字段更新
                    List<DbColumn> dbColumns = dbService.columnList(head.getTableNameOld());
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
                    dbService.createDBTable(head, fields);
                }
                break;
            case "force":
                baseMapper.executeDDL(handler.tableDropSql(new DbTable().assemble(dbConfig.getSchema(), head)));
                dbService.createDBTable(head, fields);
                break;
        }

//        updateById(Stream.of(head).peek(h -> {
//            h.setIsDbSync(CommonConstant.IS_TRUE_STR);
//            h.setTableNameOld(head.getTableName());
//        }).findFirst().get());
//
//        fieldService.updateBatchById(fields.stream().peek(f -> {
//            f.setDbFieldNameOld(f.getDbFieldName());
//            f.setDbSync(CommonConstant.IS_TRUE);
//        }).collect(Collectors.toList()));
//        indexService.createIndex(code, head.getTableName());
    }

    @Override
    public void doDbSyncAll() throws Exception {
        List<String> headIds = baseMapper.selectList(new LambdaQueryWrapper<OnlTableHead>().in(OnlTableHead::getIsDbSync, "-1", "0")).stream()
                .filter(Objects::nonNull)
                .map(OnlTableHead::getId).collect(Collectors.toList());

        if (headIds == null || headIds.isEmpty()) return;
        List<String> errorStrs = new ArrayList<>();
        for (String headId : headIds) {
            try {
                this.doDbSync(headId, "normal");
            } catch (Exception e) {
                log.error("doDbSync error : ", e);
                errorStrs.add(headId + " : " + e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

        if (!errorStrs.isEmpty())
            throw new OnlException(JSON.toJSONString(errorStrs));
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
                    .filter(a -> dbService.checkDBTableExist(a.getTableName()))
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
    public void dbTableToOnl(String... tableNames) throws Exception {
        Objects.requireNonNull(tableNames);

        List<String> list = baseMapper.selectList(new LambdaQueryWrapper<OnlTableHead>().in(OnlTableHead::getTableName, tableNames)).stream()
                .filter(Objects::nonNull)
                .map(OnlTableHead::getTableName).collect(Collectors.toList());
        if (list.size() > 0) {
            throw new OnlException("表" + list + "存在");
        }

        for (String tbName : tableNames) {
            if (ConvertUtils.isNotEmpty(tbName)) {
                int num = this.count((new LambdaQueryWrapper<OnlTableHead>()).eq(OnlTableHead::getTableName, tbName));
                if (num <= 0) {
                    log.info("表名[" + tbName + "]导入onl");
                    this.dbTableToOnl(tbName);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void dbTableToOnl(String tableName) {
        DbTable table = Optional.ofNullable(dbService.queryDBTableInfo(tableName))
                .orElseThrow(() -> new NullPointerException("表名 : " + tableName + "，获取信息为空"));
        String headId = idGenerator.next();

        this.save(new OnlTableHead().newInsert(table, headId));

        AtomicInteger orderNum = new AtomicInteger();
        fieldService.saveBatch(table.getColumnVos().stream()
                .map(column -> new OnlTableField().newInsert(column, headId, idGenerator.next(), orderNum.incrementAndGet()))
                .collect(Collectors.toList()));
    }

    @Override
    public List<OnlTableHead> queryContainFKScheduleList(String pkTableId) {
        return baseMapper.getContainFKScheduleList(pkTableId);
    }

    @Override
    public OnlTableHead queryByTableName(String tableName) throws Exception {
        QueryWrapper<OnlTableHead> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OnlTableHead::getTableName, tableName);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public OnlTableHead requireOne(String headId) {
        return Optional.ofNullable(baseMapper.selectById(headId)).filter(h -> {
            if (StringUtils.equals(CommonConstant.NEGATIVE_ONE_SIGN, h.getIsDbSync())) {
                throw new OnlException(h.getTableNameOld() + "表未创建！！！");
            }
            return true;
        }).orElseThrow(() -> new OnlException("主键为" + headId + "不存在!!!"));
    }


}

