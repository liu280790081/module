package orange.onl_table.db.tool;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.exception.OnlException;

import orange.onl_table.common.util.UUIDGenerator;
import orange.onl_table.onl.entity.OnlTableField;

import java.util.*;

@Slf4j
public class BTool {

    private BTool() {
    }

    public static void a(String tbName, List<OnlTableField> var1, StringBuffer sql) {
        sql.append("SELECT ");
        for (OnlTableField var6 : var1) {
            sql.append("`").append(var6.getDbFieldName()).append("`,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" FROM ").append(tbName);
    }

//    public static String a(List<OnlTableField> var0, Map<String, Object> var1) {
//        StringBuilder var2 = new StringBuilder();
//
//        for (OnlTableField var14 : var0) {
//            if (1 != var14.getIsQuery()) {
//                continue;
//            }
//
//            String var7 = var14.getDbFieldName(), var8 = var14.getDbType();
//            Object var9;
//            if ("single".equals(var14.getQueryMode())) {
//                var9 = var1.get(var7);
//                if (var9 != null) {
//                    boolean var10 = !CTool.a(var8);
//                    String var11 = QueryGenerator.getSingleQueryConditionSql(var7, "", var9, var10);
//                    var2.append(" AND " + var11);
//                }
//            } else {
//                var9 = var1.get(var7 + "_begin");
//                if (var9 != null) {
//                    var2.append(" AND " + var7 + ">=");
//                    if (CTool.a(var8)) {
//                        var2.append(var9.toString());
//                    } else {
//                        var2.append("'" + var9.toString() + "'");
//                    }
//                }
//
//                Object var15 = var1.get(var7 + "_end");
//                if (var15 != null) {
//                    var2.append(" AND " + var7 + "<=");
//                    if (CTool.a(var8)) {
//                        var2.append(var15.toString());
//                    } else {
//                        var2.append("'" + var15.toString() + "'");
//                    }
//                }
//            }
//
//        }
//        return var2.toString();
//    }

    public static Map<String, Object> assembleInsertSql(String var0, List<OnlTableField> var1, JSONObject var2) throws OnlException {
        StringBuilder fieldSqlStr = new StringBuilder(), valueSqlStr = new StringBuilder();

        HashMap<String, Object> var6 = new HashMap<>();
        for (OnlTableField field : var1) {
            String dbField = field.getDbFieldName();
            Object value = getFormatParam(field, var2.get(dbField));
            if (null == dbField || null == value
                    || ("".equals(value) && (CTool.a(field.getDbType()) || CTool.b(field.getDbType())))) {
                continue;
            }

            fieldSqlStr.append("`").append(dbField).append("`,");
            valueSqlStr.append("#{").append(dbField).append("},");
            var6.put(dbField, value);

        }
        fieldSqlStr.deleteCharAt(fieldSqlStr.length() - 1);
        valueSqlStr.deleteCharAt(valueSqlStr.length() - 1);
        String var16 = "insert into " + var0 + "(" + fieldSqlStr.toString() + ") values(" + valueSqlStr.toString() + ")";
        log.info("--动态表单保存sql-->" + var16);
        var6.put("execute_sql_string", var16);
        return var6;
    }

    public static Map<String, Object> assembleUpdateSql(String var0, List<OnlTableField> var1, JSONObject var2) throws OnlException {
        StringBuilder var3 = new StringBuilder();
        HashMap<String, Object> var4 = new HashMap<>();

        String pk = null;
        for (OnlTableField var8 : var1) {
            String var9 = var8.getDbFieldName();

            if (var8.getIsShowForm() == 0 || Objects.equals(CommonConstant.IS_TRUE, var8.getDbIsKey())) {
                if (Objects.equals(CommonConstant.IS_TRUE, var8.getDbIsKey())) {
                    pk = var9;
                }
                continue;
            }

            if ("".equals(var2.get(var9)) && (CTool.a(var8.getDbType()) || CTool.b(var8.getDbType()))) {
                continue;
            }

            String var10 = "#{" + var9 + "}";
            var3.append("`").append(var9).append("`=").append(var10).append(",");
            var4.put(var9, getFormatParam(var8, var2.get(var9)));
        }

        var3.deleteCharAt(var3.length() - 1);
        String var14 = "update " + var0 + " set " + var3.toString() + " where 1=1 and " + pk + "='" + var2.getString(pk) + "'";
        log.info("--动态表单编辑sql-->" + var14);
        var4.put("execute_sql_string", var14);
        return var4;
    }

    public static Object getFormatParam(OnlTableField field, Object value) throws OnlException {
        // 主键
        if (CommonConstant.IS_TRUE_STR.equals(field.getDbIsKey()) && Objects.isNull(value)) {
            return UUIDGenerator.generate();
        }

        switch (field.getDbType()) {
            case "string":
                return value == null ? null : value.toString();
            case "int":
            case "decimal":
            case "double":
                if ("".equals(value)) {
                    return 0;
                }
                return value;
            case "datetime":
                if (!value.getClass().isAssignableFrom(Date.class)) {
                    throw new OnlException(field.getDbFieldTxt() + "入参格式不正确！！！");
                }
                return value;
            case "text":
                return value;
            case "longtext":
                return value;
        }
        return value;
    }

//    public static String a(String var0, List<OnlTableField> var1, Map<String, Object> var2) {
//        StringBuilder var3 = new StringBuilder();
//        StringBuilder var4 = new StringBuilder();
//
//        for (OnlTableField var6 : var1) {
//            String var7 = var6.getDbFieldName();
//            String var8 = var6.getDbType();
//            if (var6.getIsShowList() == 1) {
//                var4.append(",").append(var7);
//            }
//
//            boolean var9;
//            String var10;
//            if (StringUtils.isNotBlank(var6.getMainField())) {
//                var9 = !CTool.a(var8);
//                var10 = QueryGenerator.getSingleQueryConditionSql(var7, "", var2.get(var7), var9);
//                if (!"".equals(var10)) {
//                    var3.append(" AND ").append(var10);
//                }
//            }
//
//            if (var6.getIsQuery() == 1) {
//                if ("single".equals(var6.getQueryMode())) {
//                    if (var2.get(var7) != null) {
//                        var9 = !CTool.a(var8);
//                        var10 = QueryGenerator.getSingleQueryConditionSql(var7, "", var2.get(var7), var9);
//                        if (!"".equals(var10)) {
//                            var3.append(" AND ").append(var10);
//                        }
//                    }
//                } else {
//                    Object var11 = var2.get(var7 + "_begin");
//                    if (var11 != null) {
//                        var3.append(" AND ").append(var7).append(">=");
//                        if (CTool.a(var8)) {
//                            var3.append(var11.toString());
//                        } else {
//                            var3.append("'").append(var11.toString()).append("'");
//                        }
//                    }
//
//                    Object var12 = var2.get(var7 + "_end");
//                    if (var12 != null) {
//                        var3.append(" AND ").append(var7).append(">=");
//                        if (CTool.a(var8)) {
//                            var3.append(var12.toString());
//                        } else {
//                            var3.append("'").append(var12.toString()).append("'");
//                        }
//                    }
//                }
//            }
//        }
//
//        return "SELECT id" + var4.toString() + " FROM " + var0 + " where 1=1  " + var3.toString();
//    }
}
