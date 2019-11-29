package orange.onl_table.common.db.mysql.query;

public interface QueryTool {

    String GT = ">",
            GE = ">=",
            LT = "<",
            LE = "<=",
            EQ = "=",
            NE = "!=",
            IN = "IN",
            NOT_IN = "IN",
            LIKE = "LIKE";

    static String getLikeConditionValue(Object value) {
        String str = value.toString().trim();
        if (str.startsWith("*") && str.endsWith("*")) {
            return "'%" + str.substring(1, str.length() - 1) + "%'";
        } else if (str.startsWith("*")) {
            return "'%" + str.substring(1) + "'";
        } else if (str.endsWith("*")) {
            return "'" + str.substring(0, str.length() - 1) + "%'";
        } else {
            return str;
        }
    }

    static String getInConditionValue(Object value) {
        String temp[] = value.toString().split(",");
        StringBuilder res = new StringBuilder();
        for (String string : temp) {
            res.append(",'").append(string).append("'");
        }
        return "(" + res.substring(1) + ")";
    }

    static String getEqConditionValue(Object value) {
        return "'" + value.toString() + "'";
    }
}
