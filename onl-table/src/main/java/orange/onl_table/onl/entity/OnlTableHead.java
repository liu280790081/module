package orange.onl_table.onl.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import orange.onl_table.common.constant.CommonConstant;
import orange.onl_table.onl.dto.DbTable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@TableName("onl_table_head")
public class OnlTableHead implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 表头id
     */
    @TableId
    private String id;

    /**
     * 模块code
     */
    private String modelCode;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表类型
     * 0单表 1主表 2附表
     */
    private Integer tableType;

    /**
     * 表说明
     */
    private String tableTxt;

    /**
     * 曾用表名 （上次所用表名）
     */
    private String tableNameOld;
//    private String idType;

    /**
     * 附表映射关系类型
     * 0一对多  1一对一
     */
    private Integer relationType;

    /**
     * 附表排序序号
     */
    private Integer tabOrderNum;

    /**
     * 查询模式
     */
    private String queryMode;

    /**
     * 树子关系字段
     */
    private String treeIdField;

    /**
     * 树父关系字段
     */
    private String treeParentIdField;

    /**
     * 树开表单列
     */
    private String treeSingleRow;

    private String formCategory;
    private String formTemplate;
    private String isCheckbox;

    /**
     * 是否分页
     */
    private String isPage;
    /**
     * 是否是树
     */
    private String isTree;

    /**
     * 是否同步
     */
    private String isDbSync;

    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer version;

    public OnlTableHead() {
    }

    public OnlTableHead(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlTableHead head = (OnlTableHead) o;
        return Objects.equals(id, head.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public OnlTableHead newInsert(String id) {
        this.id = id;
        this.tableNameOld = this.tableName;
        this.isDbSync = CommonConstant.IS_FALSE_STR;
        this.createTime = new Date();
        this.updateTime = new Date();
        this.version = 1;
        return this;
    }

    public OnlTableHead newInsert(DbTable table, String id) {
        this.id = id;
        this.tableName = table.getTableName().toLowerCase();
        this.tableTxt = table.getTableComment();
        this.tableType = 1;
        this.tableNameOld = this.tableName;
        this.formTemplate = "1";
        this.formCategory = "bdfl_include";
        this.queryMode = "group";
        this.isCheckbox = CommonConstant.IS_TRUE_STR;
        this.isDbSync = CommonConstant.IS_TRUE_STR;
        this.isTree = CommonConstant.IS_FALSE_STR;
        this.isPage = CommonConstant.IS_TRUE_STR;
        this.createTime = new Date();
        this.updateTime = new Date();
        this.version = 1;
        return this;
    }

    public OnlTableHead newUpdate(OnlTableHead now) {
        Objects.requireNonNull(now);
        if (!this.tableName.equals(now.tableName)) {
            this.isDbSync = CommonConstant.IS_FALSE_STR;
        }
        this.modelCode = now.modelCode;
        this.tableName = now.tableName;
        this.tableType = now.tableType;
        this.tableTxt = now.tableTxt;
//        this.idType = now.idType;
        this.relationType = now.relationType;
        this.tabOrderNum = now.tabOrderNum;
        this.queryMode = now.queryMode;
        this.treeIdField = now.treeIdField;
        this.treeParentIdField = now.treeParentIdField;
        this.treeSingleRow = now.treeSingleRow;
        this.formCategory = now.formCategory;
        this.formTemplate = now.formTemplate;
        this.isTree = now.isTree;
        this.isPage = now.isPage;
        this.isCheckbox = now.isCheckbox;
        this.updateTime = new Date();
        return this;
    }
}
