package orange.onl_table.common.util.excel;

public class ExcelFieldImpl implements ExcelField {

    /**
     * 导出字段标题（需要添加批注请用“**”分隔，标题**批注，仅对导出模板有效）
     */
    private String title;

    /**
     * 导出字段名（默认调用当前字段的“get”方法，如指定导出字段为对象，请填写“对象名.对象属性”，例：“area.name”、“office.name”）
     */
    private String value = "";

    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     */
    private int type = 0;

    /**
     * 导出字段对齐方式（0：自动；1：靠左；2：居中；3：靠右）
     * <p>
     * 备注：Integer/Long类型设置居右对齐（align=3）
     */
    private int align = 0;

    /**
     * 导出字段字段排序（升序）
     */
    private int sort = 0;

    /**
     * 如果是字典类型，请设置字典的type值
     */
    private String dictType = "";

    /**
     * 反射类型
     */
    private Class<?> fieldType = Class.class;

    /**
     * 字段归属组（根据分组导出导入）
     */
    private int[] groups = new int[]{};

    private ExcelFieldImpl() {
    }

    public ExcelFieldImpl(String title) {
        this.title = title;
    }


    public ExcelFieldImpl setTitle(String title) {
        this.title = title;
        return this;

    }

    public ExcelFieldImpl setValue(String value) {
        this.value = value;
        return this;
    }

    public ExcelFieldImpl setType(int type) {
        this.type = type;
        return this;
    }

    public ExcelFieldImpl setAlign(int align) {
        this.align = align;
        return this;
    }

    public ExcelFieldImpl setSort(int sort) {
        this.sort = sort;
        return this;
    }

    public ExcelFieldImpl setDictType(String dictType) {
        this.dictType = dictType;
        return this;
    }

    public ExcelFieldImpl setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public ExcelFieldImpl setGroups(int[] groups) {
        this.groups = groups;
        return this;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public int type() {
        return type;
    }

    @Override
    public int align() {
        return align;
    }

    @Override
    public int sort() {
        return sort;
    }

    @Override
    public String dictType() {
        return dictType;
    }

    @Override
    public Class<?> fieldType() {
        return fieldType;
    }

    @Override
    public int[] groups() {
        return groups;
    }
}
