package orange.onl_table.common.db;


import com.alibaba.fastjson.JSONObject;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.db.mysql.MysqlDataSqlHandler;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.entity.OnlTableField;

import java.util.List;

public interface IDataSqlHandler {

    String selectSql(DbTable table, List<OnlTableField> fields, JSONObject data);

    String countSql(DbTable table, List<OnlTableField> fields, JSONObject data);

    String insertSql(DbTable table, List<OnlTableField> fields, JSONObject data) throws OnlException;

    String updateSql(DbTable table, List<OnlTableField> fields, JSONObject data) throws OnlException;

    String deleteSql(DbTable table, List<OnlTableField> fields, JSONObject data);

    static IDataSqlHandler dataSqlHandler(String database) {
        switch (database) {
            case "mysql":
                return new MysqlDataSqlHandler();
//            case "oracle":
//                var0 = new BDbTableHandle();
//                break;
//            case "postgresql":
//                var0 = new CDbTableHandle();
//                break;
//            case "sqlserver":
//                var0 = new DDbTableHandle();
//                break;
            default:
                throw new OnlException("数据库[" + database+ "]不存在处理类");
        }
    }
}
