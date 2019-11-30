package orange.onl_table.common.db;


import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.db.mysql.MysqlDbHandler;
import orange.onl_table.common.db.postgresql.PostgresqlDbHandler;
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

    /**
     * 删除表sql
     *
     * @param table
     * @return
     */
    String tableDropSql(DbTable table);

    /**
     * 查询表详情
     *
     * @param table
     * @return
     */
    String tableOneSql(DbTable table);

    /**
     * 验证表是否存在sql
     *
     * @return
     */
    String tableCheckExistSql(DbTable table);

    /**
     * 返回数据库表sql
     *
     * @return
     */
    String tableListSql(String tableSchema, String keyword);

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
     * 字段查询集合sql
     *
     * @param table
     * @return
     */
    String columnSelectSql(DbTable table);

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

    /* ------------------------------- 工具 -------------------------------------- */

    String columnDialect(String columnName);


    static IDbHandler dbHandler(String database) {
        switch (database) {
            case "mysql":
                return new MysqlDbHandler();
            case "postgresql":
                return new PostgresqlDbHandler();
//            case "sqlserver":
//                var0 = new DDbTableHandle();
//                break;
            default:
                throw new OnlException("此数据库不存在处理类");
        }
    }


}