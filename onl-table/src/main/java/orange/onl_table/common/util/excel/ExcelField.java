package orange.onl_table.common.util.excel;


public interface ExcelField {

    String value = "";

    /**
     * 导出字段标题（需要添加批注请用“**”分隔，标题**批注，仅对导出模板有效）
     */
    String title();

    /**
     * 导出字段名（默认调用当前字段的“get”方法，如指定导出字段为对象，请填写“对象名.对象属性”，例：“area.name”、“office.name”）
     */
    default String value() {
        return "";
    }

    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     */
    default int type() {
        return 0;
    }

    /**
     * 导出字段对齐方式（0：自动；1：靠左；2：居中；3：靠右）
     * <p>
     * 备注：Integer/Long类型设置居右对齐（align=3）
     */
    default int align() {
        return 0;
    }

    /**
     * 导出字段字段排序（升序）
     */
    default int sort() {
        return 0;
    }

    /**
     * 如果是字典类型，请设置字典的type值
     */
    default String dictType() {
        return "";
    }

    /**
     * 反射类型
     */
    default Class<?> fieldType() {
        return Class.class;
    }

    /**
     * 字段归属组（根据分组导出导入）
     */
    default int[] groups() {
        return new int[]{};
    }

}
