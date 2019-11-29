package orange.onl_table.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;

import java.util.List;
import java.util.Map;

public interface OnlDataService {

    void saveDBTableData(String headId, JSONObject fkMap, JSONArray dataAry) throws Exception;

    void editDBTableData(String headId, JSONObject fkMap, JSONArray dataAry) throws Exception;

    int deleteDBTableData(String headId, String... dataId) throws Exception;

    List<Map<String, Object>> queryDBTableData(String headId, JSONObject fkJson, JSONArray dataAry) throws Exception;

    Map<String, Object> queryDBTableData(String headId, String parentPkId, String parentHeadId, String... subHeadIds) throws Exception;

    List<Map<String, Object>> dataList(String headId, JSONObject param);

    IPage<Map<String, Object>> dataPage(String headId, JSONObject param);


    /* -------------------------------- 源操作 ------------------------------------ */

    Map<String, Object> dataOne(OnlTableHead head, List<OnlTableField> fields, JSONObject param);

    Long dataCount(OnlTableHead head, List<OnlTableField> fields, JSONObject param);

    List<Map<String, Object>> dataList(OnlTableHead head, List<OnlTableField> fields, JSONObject param);

    IPage<Map<String, Object>> dataPage(OnlTableHead head, List<OnlTableField> fields, JSONObject param);

    void dataInsert(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception;

    void dataInsertTree(OnlTableHead head, List<OnlTableField> fields, String pk, JSONObject data) throws Exception;

    void dataUpdate(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception;

    void dataUpdateTree(OnlTableHead head, List<OnlTableField> fields, String pk, JSONObject data) throws Exception;

    int dataDelete(OnlTableHead head, List<OnlTableField> fields, JSONObject data) throws Exception;
}
