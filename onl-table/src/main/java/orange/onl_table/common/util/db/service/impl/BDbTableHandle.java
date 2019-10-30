package orange.onl_table.common.util.db.service.impl;//package com.ido85.icip.system.table.common.util.db.service.impl;
//
//import com.ido85.icip.system.table.common.constant.CommonConstant;
//import com.ido85.icip.system.table.common.util.db.service.IDbTableHandle;
//import com.ido85.icip.system.table.common.util.ColumnMeta;
//import org.apache.commons.lang.StringUtils;
//
//public class BDbTableHandle implements IDbTableHandle {
//    public BDbTableHandle() {
//    }
//
//    public String getAddColumnSql(ColumnMeta columnMeta) {
//        return " ADD  " + this.a(columnMeta) + "";
//    }
//
//    public String getReNameFieldName(ColumnMeta columnMeta) {
//        return "RENAME COLUMN  " + columnMeta.getOldColumnName() + " TO " + columnMeta.getColumnName() + "";
//    }
//
//    public String getUpdateColumnSql(ColumnMeta tableColumnMeta, ColumnMeta datacolumnMeta) {
//        return " MODIFY   " + this.a(tableColumnMeta, datacolumnMeta) + "";
//    }
//
//    public String getMatchClassTypeByDataType(String dataType, int digits) {
//        String var3 = "";
//        if (dataType.equalsIgnoreCase("varchar2")) {
//            var3 = "string";
//        }
//
//        if (dataType.equalsIgnoreCase("nvarchar2")) {
//            var3 = "string";
//        } else if (dataType.equalsIgnoreCase("double")) {
//            var3 = "double";
//        } else if (dataType.equalsIgnoreCase("number") && digits == 0) {
//            var3 = "int";
//        } else if (dataType.equalsIgnoreCase("number") && digits != 0) {
//            var3 = "double";
//        } else if (dataType.equalsIgnoreCase("int")) {
//            var3 = "int";
//        } else if (dataType.equalsIgnoreCase("Date")) {
//            var3 = "date";
//        } else if (dataType.equalsIgnoreCase("Datetime")) {
//            var3 = "date";
//        } else if (dataType.equalsIgnoreCase("blob")) {
//            var3 = "blob";
//        } else if (dataType.equalsIgnoreCase("clob")) {
//            var3 = "text";
//        }
//
//        return var3;
//    }
//
//    public String dropTableSQL(String tableName) {
//        return " DROP TABLE  " + tableName.toLowerCase() + " ";
//    }
//
//    public String getDropColumnSql(String fieldName) {
//        return " DROP COLUMN " + fieldName.toUpperCase() + "";
//    }
//
//    private String a(ColumnMeta var1) {
//        String var2 = "";
//        if (var1.getColumnType().equalsIgnoreCase("string")) {
//            var2 = var1.getColumnName() + " varchar2(" + var1.getColumnSize() + ")";
//        } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//            var2 = var1.getColumnName() + " datetime";
//        } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//            var2 = var1.getColumnName() + " NUMBER(" + var1.getColumnSize() + ")";
//        } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//            var2 = var1.getColumnName() + " NUMBER(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ")";
//        } else if (var1.getColumnType().equalsIgnoreCase("bigdecimal")) {
//            var2 = var1.getColumnName() + " NUMBER(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ")";
//        } else if (var1.getColumnType().equalsIgnoreCase("text")) {
//            var2 = var1.getColumnName() + " CLOB ";
//        } else if (var1.getColumnType().equalsIgnoreCase("blob")) {
//            var2 = var1.getColumnName() + " BLOB ";
//        }
//
//        var2 = var2 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " DEFAULT " + var1.getFieldDefault() : " ");
//        var2 = var2 + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? " NULL" : " NOT NULL");
//        return var2;
//    }
//
//    private String a(ColumnMeta var1, ColumnMeta var2) {
//        String var3 = "";
//        String var4 = "";
//        if (!var2.getIsNullable().equals(var1.getIsNullable())) {
//            var4 = var1.getIsNullable().equals(CommonConstant.IS_TRUE_STR) ? "NULL" : "NOT NULL";
//        }
//
//        if (var1.getColumnType().equalsIgnoreCase("string")) {
//            var3 = var1.getColumnName() + " varchar2(" + var1.getColumnSize() + ")" + var4;
//        } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//            var3 = var1.getColumnName() + " date " + var4;
//        } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//            var3 = var1.getColumnName() + " NUMBER(" + var1.getColumnSize() + ") " + var4;
//        } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//            var3 = var1.getColumnName() + " NUMBER(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") " + var4;
//        } else if (var1.getColumnType().equalsIgnoreCase("bigdecimal")) {
//            var3 = var1.getColumnName() + " NUMBER(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") " + var4;
//        } else if (var1.getColumnType().equalsIgnoreCase("blob")) {
//            var3 = var1.getColumnName() + " BLOB " + var4;
//        } else if (var1.getColumnType().equalsIgnoreCase("text")) {
//            var3 = var1.getColumnName() + " CLOB " + var4;
//        }
//
//        var3 = var3 + (StringUtils.isNotEmpty(var1.getFieldDefault()) ? " DEFAULT " + var1.getFieldDefault() : " ");
//        var3 = var3 + var4;
//        return var3;
//    }
//
//    public String getCommentSql(ColumnMeta columnMeta) {
//        return "COMMENT ON COLUMN " + columnMeta.getTableName() + "." + columnMeta.getColumnName() + " IS '" + columnMeta.getComment() + "'";
//    }
//
//    public String getSpecialHandle(ColumnMeta tableColumnMeta, ColumnMeta datacolumnMeta) {
//        return null;
//    }
//
//    public String dropIndex(String indexName, String tableName) {
//        return "DROP INDEX " + indexName;
//    }
//}
