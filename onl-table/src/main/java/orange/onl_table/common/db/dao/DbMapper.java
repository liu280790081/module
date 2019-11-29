package orange.onl_table.common.db.dao;

import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbMapper {

    /* ------------------------------数据库实体表------------------------------------ */

    List<Map<String, String>> getDBTableList(@Param("sql") String sql, @Param("tableSchema") String tableSchema, @Param("keyword") String keyword);

    DbTable getDBTableInfo(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    List<DbColumn> getDBTableColumnList(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    Boolean checkDBTableExist(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

}