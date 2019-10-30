package orange.onl_table.common.util.db;


import com.ido85.icip.system.table.common.config.DataBaseConfig;
import com.ido85.icip.system.table.entity.OnlTableField;
import com.ido85.icip.system.table.entity.OnlTableHead;
import com.ido85.icip.system.table.entity.OnlTableIndex;
import lombok.Data;

import java.util.List;

@Data
public class TableConfigModel {

    private String tableName;

    private String isDbSync;

    private String comment;

    private String version;

    private Integer type;

//    private String pkType;

//    private String pkSequence;

    private Integer relationType;

    private String subTableStr;

    private Integer tabOrder;

    private List<OnlTableField> columns;

    private List<OnlTableIndex> indexes;

    private String treeParentIdFieldName;

    private String treeIdFieldName;

    private String treeFieldName;

    private DataBaseConfig dbConfig;


    public TableConfigModel init(DataBaseConfig dbConfig, OnlTableHead head, List<OnlTableField> fields){
        this.tableName = head.getTableName();
//        this.pkType = head.getIdType();
//        this.PkSequence = head.getIdSequence();
        this.comment = head.getTableTxt();
        this.columns = fields;
        this.dbConfig = dbConfig;
        return this;
    }

}
