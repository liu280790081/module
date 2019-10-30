package orange.onl_table.common.util.db.service;


import com.ido85.icip.system.table.common.exception.OnlException;
import com.ido85.icip.system.table.common.util.db.service.impl.MysqlDbHandler;
import com.ido85.icip.system.table.dto.DbColumn;
import com.ido85.icip.system.table.dto.DbTable;

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

    static String dialect(String database) {
        switch (database) {
            case "mysql":
                return "org.hibernate.dialect.MySQL5InnoDBDialect";
            case "oracle":
                return "org.hibernate.dialect.OracleDialect";
            case "postgresql":
                return "org.hibernate.dialect.PostgreSQLDialect";
            case "sqlserver":
                return "org.hibernate.dialect.SQLServerDialect";
            default:
                throw new OnlException("此数据库方言不存在");
        }
    }
}