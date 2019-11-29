package orange.onl_table.common.db.tool;

public interface DataFormatTool {

    String[] DATE_PARSE_PATTERNS = {"yyyy", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.SSS",
            "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss.SSS",
            "yyyy.MM.dd HH:mm", "yyyy.MM", "yyyyMMddHHmmss", "yyyyMMdd", "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss:SSSZZ", "yyyy-MM-dd'T'HH:mm:ss.SSSX", "yyyy-MM-dd'T'HH:mm:ss.SSS Z"};

//    /**
//     * 数据清洗
//     * @param source
//     * @return
//     */
//    static JSONObject dataCleaning(JSONObject source) {
//        return new JSONObject(source.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> {
//            Object val = v.getValue();
//            if (val == null) return "";
//            if (val instanceof Collection || val.getClass().isArray()) {
//                return JSON.toJSONString(val);
//            }
//            return val;
//        })));
//    }

}
