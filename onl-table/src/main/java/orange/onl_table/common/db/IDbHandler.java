package orange.onl_table.common.db;


import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.db.mysql.MysqlDbHandler;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.entity.OnlTableIndex;

public interface IDbHandler {

    /**
     * 表更新sql
     *
     * @param table
     * @return
     */
    String tableUpdateSql(DbTable table);

    /**
     * 表删除sql
     *
     * @param tbName
     * @return
     */
    String tableDropSql(String tbName);


    String tableDropSql(DbTable table);

    /**
     * 返回数据库表sql
     * @return
     */
    String tableListSql(String keyword);

    /**
     * 字段添加sql
     *
     * @param column
     * @return
     */
    String columnAddSql(String tbName, DbColumn column);

    /**
     * 字段更新sql
     *
     * @param column
     * @return
     */
    String columnUpdateSql(String tbName, DbColumn column) throws OnlException;

    /**
     * 字段删除sql
     *
     * @param tbName
     * @param columnName
     * @return
     */
    String columnDropSql(String tbName, String columnName);

    /**
     * 索引删除sql
     *
     * @return
     */
    String indexCreateSql(String tbName, OnlTableIndex index);

    /**
     * 索引删除sql
     *
     * @param tbName
     * @param indexName
     * @return
     */
    String indexDropSql(String tbName, String indexName);



    static IDbHandler dbHandler(String database) {
        switch (database) {
            case "mysql":
                return new MysqlDbHandler();
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
                throw new OnlException("此数据库不存在处理类");
        }
    }


}