package orange.onl_table.common.util.db;


import lombok.Data;
import orange.onl_table.common.config.DataBaseConfig;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;
import orange.onl_table.entity.OnlTableIndex;

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


    public TableConfigModel init(DataBaseConfig dbConfig, OnlTableHead head, List<OnlTableField> fields) {
        this.tableName = head.getTableName();
//        this.pkType = head.getIdType();
//        this.PkSequence = head.getIdSequence();
        this.comment = head.getTableTxt();
        this.columns = fields;
        this.dbConfig = dbConfig;
        return this;
    }

}
