package orange.onl_table.common.util.db.tool;

import com.ido85.icip.system.table.entity.OnlTableField;
import org.jeecgframework.poi.handler.impl.ExcelDataHandlerDefaultImpl;
import org.jeecgframework.poi.util.PoiPublicUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATool extends ExcelDataHandlerDefaultImpl {
    Map<String, OnlTableField> a;

    public ATool(List<OnlTableField> var1) {
        this.a = this.a(var1);
    }

    private Map<String, OnlTableField> a(List<OnlTableField> var1) {
        HashMap var2 = new HashMap();

        for (OnlTableField var4 : var1) {
            var2.put(var4.getDbFieldTxt(), var4);
        }

        return var2;
    }

    public void setMapValue(Map<String, Object> map, String originKey, Object value) {
        if (value instanceof Double) {
            map.put(this.a(originKey), PoiPublicUtil.doubleToString((Double) value));
        } else {
            map.put(this.a(originKey), value == null ? "" : value.toString());
        }

    }

    private String a(String var1) {
        return this.a.containsKey(var1) ? "$mainTable$" + this.a.get(var1).getDbFieldName() : "$subTable$" + var1;
    }
}