package orange.onl_table.common.db.service;


import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;

import java.util.List;
import java.util.Map;

public interface DbService {

    /* ------------------------------数据库实体表------------------------------------ */

    void createDBTable(OnlTableHead head, List<OnlTableField> fields) throws Exception;

    List<Map<String, String>> queryDBTableList(String keyword);

    Map<String, String> queryDBTableMap(String keyword);

    DbTable queryDBTableInfo(String tableName);

    boolean checkDBTableExist(String tableName);

    List<DbColumn> columnList(String tableName);


}

