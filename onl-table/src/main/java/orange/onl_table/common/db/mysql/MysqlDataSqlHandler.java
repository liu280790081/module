package orange.onl_table.common.db.mysql;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.db.IDataSqlHandler;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.common.db.mysql.query.QueryGenerator;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.common.util.ConvertUtils;
import orange.onl_table.entity.OnlTableField;

import java.util.*;

public class MysqlDataSqlHandler implements IDataSqlHandler {


    @Override
    public String selectSql(DbTable table, List<OnlTableField> fields, JSONObject data) {
        StringBuilder sql = new StringBuilder("SELECT ");
        for (OnlTableField field : fields) {
            if (Objects.equals(CommonConstant.NUM_NEGATIVE_ONE, field.getDbSync())) {
                continue;
            }
            sql.append("`").append(field.getDbFieldNameOld()).append("`,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" FROM ").append(table.getTableSchema()).append(".").append(table.getOldTableName());
        sql.append(" where 1=1  ");
        for (OnlTableField field : fields) {
            Object value = data.get(field.getDbFieldNameOld());
            if (value == null || "".equals(value)) continue;

            sql.append(" AND ").append(QueryGenerator.queryConditionSql(field, value));
        }

        String var11 = Optional.ofNullable(data.get("order")).map(String::valueOf).orElse("");
        String var12 = Optional.ofNullable(data.get("column")).map(String::valueOf).orElse("");

        for (OnlTableField field : fields) {
            if (ConvertUtils.camelToUnderline(var12).equals(field.getDbFieldName())) {
                sql.append(" ORDER BY ").append(ConvertUtils.camelToUnderline(var12));
                if ("asc".equals(var11)) {
                    sql.append(" asc");
                } else {
                    sql.append(" desc");
                }
                break;
            }
        }
        return sql.toString();
    }

    @Override
    public String countSql(DbTable table, List<OnlTableField> fields, JSONObject data) {
        StringBuilder sql = new StringBuilder("SELECT count(1) ");
        sql.append(" FROM ").append(table.getTableSchema()).append(".").append(table.getOldTableName());
        sql.append(" where 1=1  ");
        for (OnlTableField field : fields) {
            Object value = data.get(field.getDbFieldNameOld());
            if (value == null || "".equals(value)) continue;

            sql.append(" AND ").append(QueryGenerator.queryConditionSql(field, value));
        }
        return sql.toString();
    }

    @Override
    public String insertSql(DbTable table, List<OnlTableField> fields, JSONObject data) throws OnlException {
        StringBuilder fieldSqlStr = new StringBuilder(), valueSqlStr = new StringBuilder();

        for (OnlTableField field : fields) {
            String dbField = field.getDbFieldNameOld();
            if (!data.containsKey(dbField)) continue;

            fieldSqlStr.append("`").append(dbField).append("`,");
            valueSqlStr.append("#{param.").append(dbField).append("},");
        }
        fieldSqlStr.deleteCharAt(fieldSqlStr.length() - 1);
        valueSqlStr.deleteCharAt(valueSqlStr.length() - 1);
        return "insert into " + table.getTableSchema() + "." + table.getOldTableName() +
                "(" + fieldSqlStr.toString() + ") " +
                "values(" + valueSqlStr.toString() + ")";
    }

    @Override
    public String updateSql(DbTable table, List<OnlTableField> fields, JSONObject data) throws OnlException {
        StringBuilder var3 = new StringBuilder();
        String pk = null;
        for (OnlTableField field : fields) {
            String fieldName = field.getDbFieldNameOld();
            if (field.getIsShowForm() == 0 || Objects.equals(CommonConstant.IS_TRUE, field.getDbIsKey())) {
                if (Objects.equals(CommonConstant.IS_TRUE, field.getDbIsKey())) {
                    pk = fieldName;
                }
                continue;
            }
            if (!data.containsKey(fieldName)) continue;

            var3.append("`").append(fieldName).append("`= #{param.").append(fieldName).append("},");
        }
        var3.deleteCharAt(var3.length() - 1);

        return "update " + table.getTableSchema() + "." + table.getOldTableName() +
                " set " + var3.toString() +
                " where 1=1 and " + pk + " in (" + getPkValSql(data.get(pk)) + ")";
    }

    @Override
    public String deleteSql(DbTable table, List<OnlTableField> fields, JSONObject data) {
        String pk = fields.stream()
                .filter(field -> Objects.equals(CommonConstant.IS_TRUE, field.getDbIsKey()))
                .findFirst().map(OnlTableField::getDbFieldNameOld).get();

        return "delete from " + table.getTableSchema() + "." + table.getOldTableName() +
                " where 1=1 and " + pk + " in (" + getPkValSql(data.get(pk)) + ")";
    }

    private String getPkValSql(Object obj) {
        return Optional.ofNullable(obj)
                .map(v -> {
                    if ("".equals(v)) {
                        throw new OnlException("主键值不存在！！！");
                    }
                    if (v instanceof Date) {
                        throw new OnlException("主键不能是时间类型");
                    }

                    if (v instanceof Collection || v.getClass().isArray()) {
                        return TypeUtils.castToJavaBean(v, JSONArray.class)
                                .toJavaList(String.class).stream()
                                .reduce("", (a, b) -> a + ",'" + b + "'").substring(1);
                    }
                    return "'" + v.toString() + "'";
                })
                .orElseThrow(() -> new OnlException("主键值不存在！！！"));
    }


}
