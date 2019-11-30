package orange.onl_table.common.db.postgresql;


import com.alibaba.fastjson.JSONObject;
import orange.onl_table.common.db.IDataSqlHandler;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.entity.OnlTableField;

import java.util.List;

public class PostgresqlDataSqlHandler implements IDataSqlHandler {


    @Override
    public String selectSql(DbTable table, List<OnlTableField> fields, JSONObject data) {
        return null;
    }

    @Override
    public String countSql(DbTable table, List<OnlTableField> fields, JSONObject data) {
        return null;
    }

    @Override
    public String insertSql(DbTable table, List<OnlTableField> fields, JSONObject data) throws OnlException {
        return null;
    }

    @Override
    public String updateSql(DbTable table, List<OnlTableField> fields, JSONObject data) throws OnlException {
        return null;
    }

    @Override
    public String deleteSql(DbTable table, List<OnlTableField> fields, JSONObject data) {
        return null;
    }
}
