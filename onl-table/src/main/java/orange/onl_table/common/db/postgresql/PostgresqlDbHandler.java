package orange.onl_table.common.db.postgresql;


import orange.onl_table.common.db.IDbHandler;
import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.entity.OnlTableIndex;

public class PostgresqlDbHandler implements IDbHandler {


    @Override
    public String tableUpdateSql(DbTable table) {
        return null;
    }

    @Override
    public String tableDropSql(String tbName) {
        return null;
    }

    @Override
    public String tableDropSql(DbTable table) {
        return null;
    }

    @Override
    public String tableOneSql(DbTable table) {
        return null;
    }

    @Override
    public String tableCheckExistSql(DbTable table) {
        return null;
    }

    @Override
    public String tableListSql(String tableSchema, String keyword) {
        return null;
    }

    @Override
    public String columnAddSql(String tbName, DbColumn column) {
        return null;
    }

    @Override
    public String columnUpdateSql(String tbName, DbColumn column) throws OnlException {
        return null;
    }

    @Override
    public String columnDropSql(String tbName, String columnName) {
        return null;
    }

    @Override
    public String columnSelectSql(DbTable table) {
        return null;
    }

    @Override
    public String indexCreateSql(String tbName, OnlTableIndex index) {
        return null;
    }

    @Override
    public String indexDropSql(String tbName, String indexName) {
        return null;
    }
}
