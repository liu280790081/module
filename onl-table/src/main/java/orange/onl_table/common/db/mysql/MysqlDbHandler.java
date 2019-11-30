package orange.onl_table.common.db.mysql;


import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.db.IDbHandler;
import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.entity.OnlTableIndex;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class MysqlDbHandler implements IDbHandler {


    @Override
    public String tableUpdateSql(DbTable table) {
        return "alter table " +
                table.getOldTableName() +
                " comment '" + table.getTableComment() + "' " +
                // 表重命名
                Stream.of(table.getOldTableName(), table.getTableName())
                        .reduce((a, b) -> (!a.equals(b)) ? ", rename as " + b : "").get();
    }

    @Override
    public String tableDropSql(String tbName) {
        return this.tableDropSql(new DbTable().newInsert(tbName));
    }

    @Override
    public String tableDropSql(DbTable table) {
        StringBuilder sql = new StringBuilder("drop table if exists ");
        if (StringUtils.isNotBlank(table.getTableSchema())) {
            sql.append(table.getTableSchema()).append(".");
        }
        sql.append(table.getOldTableName());
        return sql.toString();
    }

    @Override
    public String tableOneSql(DbTable table) {
        return "select t.table_schema, t.table_name, t.table_comment " +
                "from information_schema.tables t " +
                "where t.table_schema = '" + table.getTableSchema() +"' and table_name ='" + table.getTableName() +"'";
    }

    @Override
    public String tableCheckExistSql(DbTable table) {
        return "select if(count(table_name) > 0, 1, 0) res " +
                "from information_schema.tables " +
                "where table_schema = '" + table.getTableSchema() +"' and table_name ='" + table.getTableName() +"'";
    }

    @Override
    public String tableListSql(String tableSchema, String keyword) {
        StringBuilder sql = new StringBuilder()
                .append("select t.table_name, t.table_comment ")
                .append("from information_schema.tables t ")
                .append("left join onl_table_head th on t.table_name = th.table_name ")
                .append("where t.table_schema = '").append(tableSchema).append("' and th.table_name is null ");
        if (StringUtils.isNotBlank(keyword)) {
            sql.append("and (t.table_name like concat('%', '" + keyword +"', '%') or t.table_comment like concat('%', '" + keyword + "', '%')) ");
        }
        sql.append("order by t.table_name");
        return sql.toString();
    }

    @Override
    public String columnAddSql(String tbName, DbColumn column) {
        return "alter table " + tbName + " add column " + getColumnSql(column);
    }

    @Override
    public String columnUpdateSql(String tbName, DbColumn column) throws OnlException {
        // 判断是否需要重命名
        String updateWay = !Objects.equals(column.getColumnName(), column.getOldColumnName()) ?
                " change `" + column.getOldColumnName() + "` " : " modify column ";
        return "alter table " + tbName + updateWay + getColumnSql(column);
    }

    @Override
    public String columnDropSql(String tbName, String columnName) {
        return "alter table " + tbName + " drop column `" + columnName +"`";
    }

    @Override
    public String columnSelectSql(DbTable table) {
        return "select column_name, data_type, column_comment, numeric_precision, numeric_scale, character_maximum_length," +
                "if('YES' = is_nullable, 1, 0) is_nullable, if('PRI' = column_key, 1, 0) is_key " +
                "from information_schema.columns " +
                "where table_schema = '" + table.getTableSchema() + "' and table_name = '" + table.getOldTableName() + "'";
    }

    @Override
    public String indexCreateSql(String tbName, OnlTableIndex index) {
        String var9 = index.getIndexName();
        String var10 = index.getIndexField();
        String var11 = "normal".equals(index.getIndexType()) ? " index " : index.getIndexType() + " index ";
        return "create " + var11 + var9 + " on " + tbName + "(" + var10 + ")";
    }

    @Override
    public String indexDropSql(String tbName, String indexName) {
        return "drop index " + indexName + " on " + tbName;
    }

    private static String getColumnSql(DbColumn column) {
        String columnName = "`" + column.getColumnName() + "` ";
        String columnType = column.getColumnType().toLowerCase();
        String dataType = Optional.of(columnType).map(t -> {
            switch (t) {
                case "string":
                    return " varchar(" + column.getColumnLength() + ") ";
                case "date":
                case "datetime":
                    return " datetime ";
                case "int":
                case "integer":
                    return " int(" + column.getColumnLength() + ") ";
                case "double":
                    return " double(" + column.getColumnLength() + "," + column.getNumericScale() + ") ";
                case "decimal":
                    return " decimal(" + column.getColumnLength() + "," + column.getNumericScale() + ") ";
                case "text":
                    return " text ";
                case "longtext":
                    return " longtext ";
                case "blob":
                    return " blob ";
                default:
                    throw new OnlException(column.getColumnName() + ", 类型不存在!");
            }
        }).get();
        String isNull = Objects.equals(CommonConstant.IS_TRUE, column.getIsNullable()) ? "NULL " : "NOT NULL ";
        String comment = StringUtils.isNotBlank(column.getColumnComment()) ? "comment '" + column.getColumnComment() + "' " : " ";
        String columnDefault = StringUtils.isNotBlank(column.getColumnDefaultVal()) ? " DEFAULT " + column.getColumnDefaultVal() : " ";
        return columnName + dataType + isNull + comment + columnDefault;
    }
}
