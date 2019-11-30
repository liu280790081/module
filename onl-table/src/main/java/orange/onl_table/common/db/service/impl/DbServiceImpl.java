package orange.onl_table.common.db.service.impl;

import lombok.extern.slf4j.Slf4j;
import orange.onl_table.common.config.DataBaseConfig;
import orange.onl_table.common.db.IDbHandler;
import orange.onl_table.common.db.dao.DbMapper;
import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.db.service.DbService;
import orange.onl_table.common.engine.FreemarkerHelper;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DbServiceImpl implements DbService {

    @Autowired
    private DataBaseConfig dbConfig;
    @Resource
    private DbMapper dbMapper;

    /* ----------------------------------------数据库实体表------------------------------------------------ */

    @Override
    public void createDBTable(OnlTableHead head, List<OnlTableField> fields) throws Exception {
        HashMap<String, Object> template = new HashMap<>();
        template.put("head", head);
        template.put("columns", fields);
        String var2 = FreemarkerHelper.generateByUTF8(FreemarkerHelper.TABLE_TEMPLATE, template);
        log.info("template -> {}", var2);

        HashMap<String, java.io.Serializable> var3 = new HashMap<>();
        var3.put("hibernate.connection.driver_class", dbConfig.getDriverClassName());
        var3.put("hibernate.connection.url", dbConfig.getUrl());
        var3.put("hibernate.connection.username", dbConfig.getUsername());
        var3.put("hibernate.connection.password", dbConfig.getPassword());
        var3.put("hibernate.show_sql", true);
        var3.put("hibernate.format_sql", true);
        var3.put("hibernate.dialect", dbConfig.getDialect());
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

    @Override
    public DbTable queryDBTableInfo(String tableName) {
        String sql = IDbHandler.dbHandler(dbConfig.getDbName())
                .tableOneSql(new DbTable().assemble(dbConfig.getSchema(), tableName));
        return dbMapper.tableOne(sql);
    }

    @Override
    public boolean checkDBTableExist(String tableName) {
        String sql = IDbHandler.dbHandler(dbConfig.getDbName())
                .tableCheckExistSql(new DbTable().assemble(dbConfig.getSchema(), tableName));
        return dbMapper.tableCheckExist(sql);
    }

    private final static String[] STARTS_WITH_TABLE_STR = new String[]{"sys", "onl", "crawler"};

    @Override
    public List<Map<String, String>> queryDBTableList(String keyword) {
        String sql = IDbHandler.dbHandler(dbConfig.getDbName()).tableListSql(dbConfig.getSchema(), keyword);
        return dbMapper.tableList(sql).stream()
                .filter(i -> !StringUtils.startsWithAny(i.getTableName(), STARTS_WITH_TABLE_STR))
                .map(m -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("tableName", m.getTableName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DbColumn> columnList(String tableName) {
        String sql = IDbHandler.dbHandler(dbConfig.getDbName())
                .columnSelectSql(new DbTable().assemble(dbConfig.getSchema(), tableName));
        return dbMapper.columnList(sql);
    }

}

