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


    Object selectObject(@Param("sql") String sql);

    DbTable tableOne(@Param("sql") String sql);

    Boolean tableCheckExist(@Param("sql") String sql);

    List<DbTable> tableList(@Param("sql") String sql);

    List<DbColumn> columnList(@Param("sql") String sql);

}