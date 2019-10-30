package orange.onl_table.onl.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import orange.onl_table.onl.entity.OnlTableField;
import orange.onl_table.onl.entity.OnlTableHead;

import java.util.List;
import java.util.Map;

public interface OnlTableFieldService extends IService<OnlTableField> {

    Map<String, Object> queryAuto(String var1, String var2, Map<String, Object> var3, List<String> var4);

    void saveFormData(String var1, String var2, JSONObject var3) throws Exception;

    void saveFormData(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception;

    void saveTreeFormData(String var1, String var2, JSONObject var3, String var4, String var5) throws Exception;

    void saveTreeFormData(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception;

    void editFormData(String var1, String var2, JSONObject var3) throws Exception;

    void editFormData(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception;

    void editTreeFormData(String var1, String var2, JSONObject var3, String var4, String var5) throws Exception;

    void editTreeFormData(OnlTableHead head, List<OnlTableField> fields, String pk, JSONObject data) throws Exception;

    List<Map<String, String>> getAutoListQueryInfo(String var1) throws Exception;

}
