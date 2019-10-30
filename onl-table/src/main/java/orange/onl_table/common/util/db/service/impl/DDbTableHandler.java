package orange.onl_table.common.util.db.service.impl;//package com.ido85.icip.system.table.common.util.db.service.impl;
//
//import com.ido85.icip.system.table.common.constant.CommonConstant;
//import com.ido85.icip.system.table.common.util.db.service.IDbTableHandle;
//import com.ido85.icip.system.table.common.util.ColumnMeta;
//import org.apache.commons.lang3.StringUtils;
//
//public class DDbTableHandle implements IDbTableHandle {
//    public DDbTableHandle() {
//    }
//
//    public String getAddColumnSql(ColumnMeta columnMeta) {
//        return " ADD  " + this.a(columnMeta) + ";";
//    }
//
//    public String getReNameFieldName(ColumnMeta columnMeta) {
//        return "  sp_rename '" + columnMeta.getTableName() + "." + columnMeta.getOldColumnName() + "', '" + columnMeta.getColumnName() + "', 'COLUMN';";
//    }
//
//    public String getUpdateColumnSql(ColumnMeta tableColumnMeta, ColumnMeta datacolumnMeta) {
//        return " ALTER COLUMN  " + this.a(tableColumnMeta, datacolumnMeta) + ";";
//    }
//
//    public String getMatchClassTypeByDataType(String dataType, int digits) {
//        String var3 = "";
//        if (dataType.equalsIgnoreCase("varchar")) {
//            var3 = "string";
//        } else if (dataType.equalsIgnoreCase("float")) {
//            var3 = "double";
//        } else if (dataType.equalsIgnoreCase("int")) {
//            var3 = "int";
//        } else if (dataType.equalsIgnoreCase("Date")) {
//            var3 = "date";
//        } else if (dataType.equalsIgnoreCase("Datetime")) {
//            var3 = "date";
//        } else if (dataType.equalsIgnoreCase("numeric")) {
//            var3 = "bigdecimal";
//        } else if (dataType.equalsIgnoreCase("varbinary")) {
//            var3 = "blob";
//        }
//
//        return var3;
//    }
//
//    public String dropTableSQL(String tableName) {
//        return " DROP TABLE " + tableName + " ;";
//    }
//
//    public String getDropColumnSql(String fieldName) {
//        return " DROP COLUMN " + fieldName + ";";
//    }
//
//    private String a(ColumnMeta var1, ColumnMeta var2) {
//        String var3 = "";
//        if (var1.getColumnType().equalsIgnoreCase("string")) {
//            var3 = var1.getColumnName() + " varchar(" + var1.getColumnSize() + ") " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//            var3 = var1.getColumnName() + " datetime " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//            var3 = var1.getColumnName() + " int " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//            var3 = var1.getColumnName() + " float " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("bigdecimal")) {
//            var3 = var1.getColumnName() + " numeric(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("text")) {
//            var3 = var1.getColumnName() + " text " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("blob")) {
//            var3 = var1.getColumnName() + " varbinary(" + var1.getColumnSize() + ") " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        }
//
//        return var3;
//    }
//
//    private String a(ColumnMeta var1) {
//        String var2 = "";
//        if (var1.getColumnType().equalsIgnoreCase("string")) {
//            var2 = var1.getColumnName() + " varchar(" + var1.getColumnSize() + ") " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//            var2 = var1.getColumnName() + " datetime " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//            var2 = var1.getColumnName() + " int " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//            var2 = var1.getColumnName() + " float " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("bigdecimal")) {
//            var2 = var1.getColumnName() + " numeric(" + var1.getColumnSize() + "," + var1.getDecimalDigits() + ") " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("text")) {
//            var2 = var1.getColumnName() + " text " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("blob")) {
//            var2 = var1.getColumnName() + " varbinary(" + var1.getColumnSize() + ") " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        }
//
//        return var2;
//    }
//
//    private String b(ColumnMeta var1) {
//        String var2 = "";
//        if (var1.getColumnType().equalsIgnoreCase("string")) {
//            var2 = var1.getColumnName() + " varchar(" + var1.getColumnSize() + ") " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("date")) {
//            var2 = var1.getColumnName() + " datetime " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("int")) {
//            var2 = var1.getColumnName() + " int " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        } else if (var1.getColumnType().equalsIgnoreCase("double")) {
//            var2 = var1.getColumnName() + " float " + (CommonConstant.IS_TRUE_STR.equals(var1.getIsNullable()) ? "NULL" : "NOT NULL");
//        }
//
//        return var2;
//    }
//
//    public String getCommentSql(ColumnMeta columnMeta) {
//        StringBuffer var2 = new StringBuffer("EXECUTE ");
//        if (StringUtils.isBlank(columnMeta.getOldColumnName())) {
//            var2.append("sp_addextendedproperty");
//        } else {
//            var2.append("sp_updateextendedproperty");
//        }
//
//        var2.append(" N'MS_Description', '");
//        var2.append(columnMeta.getComment());
//        var2.append("', N'SCHEMA', N'dbo', N'TABLE', N'");
//        var2.append(columnMeta.getTableName());
//        var2.append("', N'COLUMN', N'");
//        var2.append(columnMeta.getColumnName() + "'");
//        return var2.toString();
//    }
//
//    public String getSpecialHandle(ColumnMeta tableColumnMeta, ColumnMeta datacolumnMeta) {
//        return null;
//    }
//
//    public String dropIndex(String indexName, String tableName) {
//        return "DROP INDEX " + indexName + " ON " + tableName;
//    }
//}
