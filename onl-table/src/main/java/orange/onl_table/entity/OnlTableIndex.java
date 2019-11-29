package orange.onl_table.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import orange.onl_table.common.constant.CommonConstant;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Data
@TableName("onl_table_index")
public class OnlTableIndex implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;
    private String headId;
    private String indexName;
    private String indexField;
    private String indexType;

    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    public OnlTableIndex() {
    }

    public OnlTableIndex(String id) {
        this.id = id;
    }

    public OnlTableIndex(OnlTableIndex that) {
        this.id = that.id;
        this.indexName = that.indexName;
        this.indexField = that.indexField;
        this.indexType = that.indexType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlTableIndex that = (OnlTableIndex) o;
        return Objects.equals(id, that.id) ||
                (Objects.equals(indexName, that.indexName)
                        && Objects.equals(indexField, that.indexField)
                        && Objects.equals(indexType, that.indexType));
    }

    public OnlTableIndex newInsert(String id, String headId){
        this.id = id;
        this.headId = headId;
        this.createTime = new Date();
        this.updateTime = new Date();
        return this;
    }

    public OnlTableIndex newInsert(OnlTableIndex now, String id, String headId){
        this.id = id;
        this.headId = headId;
        this.indexName = now.indexName;
        this.indexField = now.indexField;
        this.indexType = now.indexType;
        this.createBy = now.createBy;
        this.createTime = new Date();
        this.updateBy = now.updateBy;
        this.updateTime = new Date();
        return this;
    }

    public OnlTableIndex newUpdate(OnlTableIndex now, OnlTableHead head){
        if (!Objects.equals(
                Arrays.asList(this.indexName, this.indexField, this.indexType),
                Arrays.asList(now.indexName, now.indexField, now.indexType))) {
            // 需要同步
            head.setIsDbSync(CommonConstant.IS_FALSE_STR);
        }
        this.indexName = now.indexName;
        this.indexField = now.indexField;
        this.indexType = now.indexType;
        this.updateBy = now.updateBy;
        this.updateTime = new Date();
        return this;
    }
}
