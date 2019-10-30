package orange.onl_table.common.util.db.service.impl;//package com.ido85.icip.system.table.common.util.db.service.impl;
//
//import com.ido85.icip.system.table.common.exception.OnlException;
//import com.ido85.icip.system.table.common.util.db.service.IDbTableHandle;
//import com.ido85.icip.system.table.common.util.ColumnMeta;
//import org.apache.commons.lang.StringUtils;
//
//public class CDbTableHandle implements IDbTableHandle {
//    public CDbTableHandle() {
//    }
//
//    public String getAddColumnSql(ColumnMeta columnMeta) {
//        return " ADD COLUMN " + this.a(columnMeta) + ";";
//    }
//
//    public String getReNameFieldName(ColumnMeta columnMeta) {
//        return " RENAME  COLUMN  " + columnMeta.getOldColumnName() + " to " + columnMeta.getColumnName() + ";";
//    }
//
//    public String getUpdateColumnSql(ColumnMeta tableColumnMeta, ColumnMeta dataColumnMeta) throws OnlException {
//        return "  ALTER  COLUMN   " + this.a(tableColumnMeta, dataColumnMeta) + ";";
//    }
//
//    public String getSpecialHandle(ColumnMeta tableColumnMeta, ColumnMeta dataColumnMeta) {
//        return "  ALTER  COLUMN   " + this.b(tableColumnMeta, dataColumnMeta) + ";";
//    }
//
//    public String getMatchClassTypeByDataType(String dataType, int digits) {
//        String var3 = "";
//        if (dataType.equalsIgnoreCase("varchar")) {
//            var3 = "string";
//        } else if (dataType.equalsIgnoreCase("double")) {
//            var3 = "double";
//        } else if (dataType.contains("int")) {
//            var3 = "int";
//        } else if (dataType.equalsIgnoreCase("Date")) {
//            var3 = "date";
//        } else if (dataType.equalsIgnoreCase("timestamp")) {
//            var3 = "date";
//        } else if (dataType.equalsIgnoreCase("bytea")) {
//            var3 = "blob";
//        } else if (dataType.equalsIgnoreCase("text")) {
//            var3 = "text";
//        } else if (dataType.equalsIgnoreCase("decimal")) {
//            var3 = "bigdecimal";
//        } else if (dataType.equalsIgnoreCase("numeric")) {
//            var3 = "bigdecimal";
//        }
//
//        return var3;
//    }
//
//    public String dropTableSQL(String tableName) {
//        return " DROP TABLE  " + tableName + " ;";
//    }
//
//    public String getDropColumnSql(String fieldName) {
//        return " DROP COLUMN " + fieldName + ";";
//    }
//
//    private String a(ColumnMeta var1, ColumnMeta var2) throws OnlException {
//        String var3 = "";
//        if (var1.getColumnType().equalsIgnoreCase("string")) {
//            var3 = var1.getColumnName() + "  type character varying(" + var1.getColumnSize() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//            var3 = var1.getColumnName() + "  type datetime ";
//        } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//            var3 = var1.getColumnName() + " type int4 ";
//        } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//            var3 = var1.getColumnName() + " type  numeric(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("BigDecimal")) {
//            var3 = var1.getColumnName() + " type  decimal(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("text")) {
//            var3 = var1.getColumnName() + "  type text(" + var1.getColumnSize() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("blob")) {
//            throw new OnlException("blob类型不可修改");
//        }
//
//        return var3;
//    }
//
//    private String b(ColumnMeta var1, ColumnMeta var2) {
//        String var3 = "";
//        if (!var1.a(var2)) {
//            if (var1.getColumnType().equalsIgnoreCase("string")) {
//                var3 = var1.getColumnName();
//                var3 = var3 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " SET DEFAULT " + var1.getFieldDefault() : " DROP DEFAULT");
//            } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//                var3 = var1.getColumnName();
//                var3 = var3 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " SET DEFAULT " + var1.getFieldDefault() : " DROP DEFAULT");
//            } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//                var3 = var1.getColumnName();
//                var3 = var3 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " SET DEFAULT " + var1.getFieldDefault() : " DROP DEFAULT");
//            } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//                var3 = var1.getColumnName();
//                var3 = var3 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " SET DEFAULT " + var1.getFieldDefault() : " DROP DEFAULT");
//            } else if (var1.getColumnType().equalsIgnoreCase("bigdecimal")) {
//                var3 = var1.getColumnName();
//                var3 = var3 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " SET DEFAULT " + var1.getFieldDefault() : " DROP DEFAULT");
//            } else if (var1.getColumnType().equalsIgnoreCase("text")) {
//                var3 = var1.getColumnName();
//                var3 = var3 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " SET DEFAULT " + var1.getFieldDefault() : " DROP DEFAULT");
//            }
//        }
//
//        return var3;
//    }
//
//    private String a(ColumnMeta var1) {
//        String var2 = "";
//        if (var1.getColumnType().equalsIgnoreCase("string")) {
//            var2 = var1.getColumnName() + " character varying(" + var1.getColumnSize() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//            var2 = var1.getColumnName() + " datetime ";
//        } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//            var2 = var1.getColumnName() + " int4";
//        } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//            var2 = var1.getColumnName() + " numeric(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("bigdecimal")) {
//            var2 = var1.getColumnName() + " decimal(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("blob")) {
//            var2 = var1.getColumnName() + " bytea(" + var1.getColumnSize() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("text")) {
//            var2 = var1.getColumnName() + " text(" + var1.getColumnSize() + ") ";
//        }
//
//        var2 = var2 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " DEFAULT " + var1.getFieldDefault() : " ");
//        return var2;
//    }
//
//    private String b(ColumnMeta var1) {
//        String var2 = "";
//        if (var1.getColumnType().equalsIgnoreCase("string")) {
//            var2 = var1.getColumnName() + " character varying(" + var1.getColumnSize() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//            var2 = var1.getColumnName() + " datetime ";
//        } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//            var2 = var1.getColumnName() + " int(" + var1.getColumnSize() + ") ";
//        } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//            var2 = var1.getColumnName() + " numeric(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") ";
//        }
//
//        return var2;
//    }
//
//    public String getCommentSql(ColumnMeta columnMeta) {
//        return "COMMENT ON COLUMN " + columnMeta.getTableName() + "." + columnMeta.getColumnName() + " IS '" + columnMeta.getComment() + "'";
//    }
//
//    public String dropIndex(String indexName, String tableName) {
//        return "DROP INDEX " + indexName;
//    }
//}
//
