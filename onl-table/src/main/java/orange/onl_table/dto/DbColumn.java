package orange.onl_table.dto;

import lombok.Data;
import orange.onl_table.entity.OnlTableField;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
public class DbColumn {

    private String columnName;

    private String columnComment;

    private String dataType;

    private Integer numericPrecision;

    private Integer numericScale;

    private Long characterMaximumLength;

    private Integer isNullable;

    private Integer isKey;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 字段长度
     */
    private Long columnLength;

    /**
     * 字段默认值
     */
    private String columnDefaultVal;

    /**
     * 称用字段名
     */
    private String oldColumnName;

    public DbColumn() {
    }

    public DbColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbColumn dbColumn = (DbColumn) o;
        return Objects.equals(StringUtils.lowerCase(columnName), StringUtils.lowerCase(dbColumn.columnName));
    }

    public DbColumn newInsert(OnlTableField field) {
        this.columnName = field.getDbFieldName();
        this.columnComment = field.getDbFieldTxt();
        this.numericScale = field.getDbPointLength();
        this.isNullable = field.getDbIsNull();
        this.isKey = field.getDbIsKey();
        this.columnType = field.getDbType();
        this.columnLength = field.getDbLength();
        this.columnDefaultVal = field.getDbDefaultVal();
        this.oldColumnName = field.getDbFieldNameOld();
        return this;
    }
}

