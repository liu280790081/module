package orange.onl_table.db.service.impl;

import orange.onl_table.common.exception.OnlException;
import orange.onl_table.db.service.IDbHandler;
import orange.onl_table.onl.dto.DbColumn;
import orange.onl_table.onl.dto.DbTable;

public class PgSqlDbHandle implements IDbHandler {

    @Override
    public String tableUpdateSql(DbTable table) {
        return null;
    }

    @Override
    public String tableDropSql(String tbName) {
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
    public String indexDropSql(String tbName, String indexName) {
        return null;
    }
}

