package orange.onl_table.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.common.util.ConvertUtils;
import orange.onl_table.dto.DbColumn;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Data
@TableName("onl_table_field")
public class OnlTableField implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    private String headId;
    private String dbFieldName;
    private String dbFieldTxt;
    private Integer dbIsKey;
    private Integer dbIsNull;
    private String dbType;
    private Long dbLength;
    private Integer dbPointLength;
    private String dbDefaultVal;
    private String dbFieldNameOld;
    private Integer dbSync;
    private String dictField;
    private String dictTable;
    private String dictText;
    private String fieldName;
    private String fieldShowType;
    private String fieldHref;
    private Integer fieldLength;
    private String fieldValidType;
    private String fieldMustInput;
    private String fieldExtendJson;
    private String fieldValueRuleCode;
    private Integer isQuery;
    private Integer isShowForm;
    private Integer isShowList;
    private String queryMode;
    private String mainTable;
    private String mainField;
    private Integer orderNum;
    private String createBy;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    private String updateBy;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;

    public OnlTableField() {
    }

    public OnlTableField(String id, String dbFieldName) {
        this.id = id;
        this.dbFieldName = dbFieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlTableField field = (OnlTableField) o;
        return Objects.equals(id, field.id) ||
                Objects.equals(dbFieldName, field.dbFieldName);
    }

    public OnlTableField newInsert(String id, String headId, Integer orderNum) {
        this.id = id;
        this.headId = headId;
        this.dbFieldNameOld = this.dbFieldName;
        this.dbSync = CommonConstant.IS_FALSE;
        this.fieldName = ConvertUtils.camelName(this.dbFieldName);
        this.orderNum = Optional.ofNullable(this.orderNum).orElse(orderNum);
        this.createTime = new Date();
        this.updateTime = new Date();
        return this;
    }

    public OnlTableField newInsert(DbColumn column, String headId, String id, Integer orderNum) {
        this.id = id;
        this.headId = headId;
        this.dbFieldName = column.getColumnName().toLowerCase();
        this.dbFieldTxt = StringUtils.isBlank(column.getColumnComment()) ? column.getColumnName() : column.getColumnComment();
        this.dbType = this.getDbTypeByDataType(column.getDataType());
        this.dbIsKey = column.getIsKey();
        this.dbLength = Stream.of(column.getCharacterMaximumLength(), column.getNumericPrecision())
                .filter(Objects::nonNull).findAny().orElse(0L).longValue();
        this.dbPointLength = column.getNumericScale();
        this.dbIsNull = column.getIsNullable();
        this.dbFieldNameOld = this.dbFieldName;
        this.dbSync = CommonConstant.IS_FALSE;
        this.fieldName = ConvertUtils.camelName(column.getColumnName().toLowerCase());
        this.fieldMustInput = "0";
        this.fieldLength = 120;
        this.fieldShowType = "text";
        this.orderNum = orderNum;
        this.queryMode = "group";
        this.isShowForm = CommonConstant.IS_TRUE;
        this.isQuery = CommonConstant.IS_FALSE;
        this.isShowList = CommonConstant.IS_TRUE;
        return this;
    }

    public OnlTableField newInsert(OnlTableField now, String id, OnlTableHead head) {
        Objects.requireNonNull(now);
        Objects.requireNonNull(head);
        head.setIsDbSync(CommonConstant.IS_FALSE_STR);

        this.id = id;
        this.headId = head.getId();
        this.dbFieldName = now.dbFieldName;
        this.dbFieldTxt = now.dbFieldTxt;
        this.dbIsKey = now.dbIsKey;
        this.dbIsNull = now.dbIsNull;
        this.dbType = now.dbType;
        this.dbLength = now.dbLength;
        this.dbPointLength = now.dbPointLength;
        this.dbDefaultVal = now.dbDefaultVal;
        this.dbFieldNameOld = now.dbFieldName;
        this.dbSync = CommonConstant.IS_FALSE;
        this.dictField = now.dictField;
        this.dictTable = now.dictTable;
        this.dictText = now.dictText;
        this.fieldName = now.fieldName;
        this.fieldShowType = now.fieldShowType;
        this.fieldHref = now.fieldHref;
        this.fieldLength = now.fieldLength;
        this.fieldValidType = now.fieldValidType;
        this.fieldMustInput = now.fieldMustInput;
        this.fieldExtendJson = now.fieldExtendJson;
        this.fieldValueRuleCode = now.fieldValueRuleCode;
        this.isQuery = now.isQuery;
        this.isShowForm = now.isShowForm;
        this.isShowList = now.isShowList;
        this.queryMode = now.queryMode;
        this.mainTable = now.mainTable;
        this.mainField = now.mainField;
        this.orderNum = Optional.ofNullable(now.orderNum).orElse(99);
        this.createTime = new Date();
        this.updateTime = new Date();
        return this;
    }

    public OnlTableField newUpdate(OnlTableField now, OnlTableHead head) {
        Objects.requireNonNull(now);
        if (!Objects.equals(
                Arrays.asList(this.dbFieldName, this.dbFieldTxt, this.dbLength,
                        this.dbPointLength, this.dbType, this.dbIsNull, this.dbIsKey,
                        this.mainTable, this.mainField, this.dbDefaultVal),
                Arrays.asList(now.dbFieldName, now.dbFieldTxt, now.dbLength,
                        now.dbPointLength, now.dbType, now.dbIsNull, now.dbIsKey,
                        now.mainTable, now.mainField, now.dbDefaultVal))) {
            // 需要同步
            head.setIsDbSync(CommonConstant.IS_FALSE_STR);
        }

        this.dbFieldName = now.dbFieldName;
        this.dbFieldTxt = now.dbFieldTxt;
        this.dbIsKey = now.dbIsKey;
        this.dbIsNull = now.dbIsNull;
        this.dbType = now.dbType;
        this.dbLength = now.dbLength;
        this.dbPointLength = now.dbPointLength;
        this.dbDefaultVal = now.dbDefaultVal;
        this.dictField = now.dictField;
        this.dictTable = now.dictTable;
        this.dictText = now.dictText;
        this.fieldName = now.fieldName;
        this.fieldShowType = now.fieldShowType;
        this.fieldHref = now.fieldHref;
        this.fieldLength = now.fieldLength;
        this.fieldValidType = now.fieldValidType;
        this.fieldMustInput = now.fieldMustInput;
        this.fieldExtendJson = now.fieldExtendJson;
        this.fieldValueRuleCode = now.fieldValueRuleCode;
        this.isQuery = now.isQuery;
        this.isShowForm = now.isShowForm;
        this.isShowList = now.isShowList;
        this.queryMode = now.queryMode;
        this.mainTable = now.mainTable;
        this.mainField = now.mainField;
        this.orderNum = Optional.ofNullable(now.orderNum).orElse(99);
        this.updateTime = new Date();
        return this;
    }


    private String getDbTypeByDataType(String dataType) {
        dataType = dataType.toLowerCase();
        switch (dataType) {
            case "varchar":
                return "string";
            case "double":
                return "double";
            case "int":
            case "integer":
                return "int";
            case "date":
            case "datetime":
                return "date";
            case "decimal":
                return "decimal";
            case "longtext":
            case "text":
                return "text";
            case "blob":
                return "blob";
            default:
                return "string";
        }

    }

}
