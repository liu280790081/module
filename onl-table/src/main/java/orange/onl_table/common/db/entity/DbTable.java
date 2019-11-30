package orange.onl_table.common.db.entity;

import lombok.Data;
import orange.onl_table.entity.OnlTableHead;

import java.util.List;

@Data
public class DbTable {

    private String tableSchema;

    private String tableName;

    private String tableComment;

    /**
     * 源表用名
     */
    private String oldTableName;

    private List<DbColumn> columnVos;

    public DbTable newInsert(String oldTableName) {
        this.oldTableName = oldTableName;
        return this;
    }

    public DbTable newInsert(OnlTableHead head) {
        this.tableName = head.getTableName();
        this.tableComment = head.getTableTxt();
        this.oldTableName = head.getTableNameOld();
        return this;
    }

    public DbTable assemble(String tableSchema, OnlTableHead head) {
        this.tableSchema = tableSchema;
        this.tableName = head.getTableName();
        this.tableComment = head.getTableTxt();
        this.oldTableName = head.getTableNameOld();
        return this;
    }

    public DbTable assemble(String tableSchema, String tableName) {
        this.tableSchema = tableSchema;
        this.tableName = tableName;
        return this;
    }
}

