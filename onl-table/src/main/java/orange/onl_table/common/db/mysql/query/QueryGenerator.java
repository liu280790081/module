package orange.onl_table.common.db.mysql.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.entity.OnlTableField;

import java.util.Collection;
import java.util.Map;

@Slf4j
public class QueryGenerator {

    private static final String STAR = "*";
    private static final String COMMA = ",";
    private static final String NOT_EQUAL = "!";
    private static final String BLANK_SPACE = " ";

    /**
     * 获取查询条件
     *
     * @param field
     * @param value
     * @return
     */
    public static String queryConditionSql(OnlTableField field, Object value) {
        if (value == null) return "";

        switch (field.getQueryMode()) {
            case "single":
                return singleSql(field, value);
            case "fuzzy":
                return fuzzySql(field, value, 0);
            case "right_fuzzy":
                return fuzzySql(field, value, 1);
            case "left_fuzzy":
                return fuzzySql(field, value, 2);
            case "group":
                return groupSql(field, value);
            default:
                return singleSql(field, value);
        }
    }

    /**
     * single
     */
    private static String singleSql(OnlTableField field, Object value) {
        StringBuilder sql = new StringBuilder("`" + field.getDbFieldNameOld() + "` ");

        if (value instanceof Collection || value.getClass().isArray()) {
            log.info("是个Array： {}", JSON.toJSONString(value));
            sql.append(QueryTool.IN).append(BLANK_SPACE)
                    .append("(").append(TypeUtils.castToJavaBean(value, JSONArray.class).stream()
                    .map(Object::toString)
                    .reduce("", (a, b) -> a + ",'" + b + "'")
                    .substring(1)).append(")");
        } else if (value instanceof Map) {
            log.info("是个Map： {}", JSON.toJSONString(value));
        } else {
            String val = TypeUtils.castToString(value);
            // like
            if (val.contains(STAR)) {
                sql.append(QueryTool.LIKE).append(BLANK_SPACE)
                        .append(QueryTool.getLikeConditionValue(value));
            } else {
                // !=
                if (val.startsWith(NOT_EQUAL)) {
                    if (val.contains(COMMA)) {
                        sql.append(QueryTool.NOT_IN).append(BLANK_SPACE)
                                .append(QueryTool.getInConditionValue(value));
                    } else {
                        sql.append(QueryTool.NE).append(BLANK_SPACE)
                                .append(QueryTool.getEqConditionValue(value));
                    }
                } else {
                    if (val.contains(COMMA)) {
                        sql.append(QueryTool.IN).append(BLANK_SPACE)
                                .append(QueryTool.getInConditionValue(value));
                    } else {
                        sql.append(QueryTool.EQ).append(BLANK_SPACE)
                                .append(QueryTool.getEqConditionValue(value));
                    }
                }
            }
        }
        return sql.toString();
    }

    /**
     * fuzzy
     * <p>
     * mode 0 fuzzy 1 right_fuzzy 2 left_fuzzy
     */
    private static String fuzzySql(OnlTableField field, Object value, int mode) {
        String identifierLeft, identifierRight;
        switch (mode) {
            case 1:
                identifierLeft = "'";
                identifierRight = "%'";
                break;
            case 2:
                identifierLeft = "'%";
                identifierRight = "'";
                break;
            default:
                identifierLeft = "'%";
                identifierRight = "%'";
                break;
        }

        StringBuilder sql = new StringBuilder();
        if (value instanceof Collection || value.getClass().isArray()) {
            sql.append("( 1!=1 ");
            JSONArray ary = (JSONArray) JSON.toJSON(value);
            for (Object obj : ary) {
                sql.append("or `").append(field.getDbFieldNameOld()).append("`").append(BLANK_SPACE)
                        .append(QueryTool.LIKE).append(BLANK_SPACE)
                        .append(identifierLeft).append(obj).append(identifierRight).append(BLANK_SPACE);
            }
        } else if (value instanceof Map) {
            log.info("是个Map： {}", JSON.toJSONString(value));
        } else {
            sql.append("`").append(field.getDbFieldNameOld()).append("`").append(BLANK_SPACE)
                    .append(QueryTool.LIKE).append(BLANK_SPACE)
                    .append(identifierLeft).append(value).append(identifierRight).append(BLANK_SPACE);
        }
        return sql.toString();
    }


    /**
     * group
     */
    private static String groupSql(OnlTableField field, Object value) {

        return "";
    }
}
