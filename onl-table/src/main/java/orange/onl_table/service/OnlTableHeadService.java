package orange.onl_table.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.common.util.db.TableConfigModel;
import orange.onl_table.dto.DbTable;
import orange.onl_table.dto.TableModel;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OnlTableHeadService extends IService<OnlTableHead> {

    boolean addAll(TableModel var1);

    boolean editAll(TableModel var1);

    void doDbSync(String var1, String var2) throws Exception;

    void deleteRecordAndTable(List<String> var1, boolean isDelete) throws OnlException, SQLException;

    void saveBatchDbTable2Online(String... tableNames) throws Exception;

    void saveDbTable2Online(String var1) throws Exception;

    void saveBatchOnlineTable(String var1, List<OnlTableField> var2, List<Map<String, Object>> var3);

    List<Map<String, Object>> queryListData(String var1);

    List<OnlTableHead> queryContainFKScheduleList(String pkTableId);


    /* ------------------------------数据库实体表------------------------------------ */

    void createDBTable(TableConfigModel config) throws Exception;

    List<Map<String, String>> queryDBTableList(String keyword);

    Map<String, String> queryDBTableMap(String keyword);

    DbTable queryDBTableInfo(String tableName);

    boolean checkDBTableExist(String tableName);

    /* ------------------------------数据库实体表数据------------------------------------ */

    String saveDBTableData(String var1, JSONObject var2) throws Exception;

    boolean saveDBTableData(String headId, JSONObject fkMap, JSONArray dataAry) throws Exception;

    void editDBTableData(String var1, JSONObject var2) throws Exception;

    void editDBTableData(String headId, JSONObject fkMap, JSONArray dataAry) throws Exception;

    int deleteDBTableData(String headId, String... dataId) throws Exception;

    Map<String, Object> queryDBTableData(String var1, String var2) throws Exception;

    Map<String, Object> queryDBTableData(String headId, String pkId, String... subHeadIds) throws Exception;

    Map<String, Object> queryDBTableAuto(String headId, JSONObject params) throws Exception;
}

