package orange.onl_table.dto;

import com.ido85.icip.system.table.entity.OnlTableHead;
import lombok.Data;

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

    public DbTable newInsert(OnlTableHead head) {
        this.tableName = head.getTableName();
        this.tableComment = head.getTableTxt();
        this.oldTableName = head.getTableNameOld();
        return this;
    }
}

