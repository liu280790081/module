package orange.onl_table.common.db.dao;

import orange.onl_table.common.db.entity.DbColumn;
import orange.onl_table.common.db.entity.DbTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbMapper {

    @Select("${sql}")
    Object selectObject(@Param("sql") String sql);

    @Select("${sql}")
    DbTable tableOne(@Param("sql") String sql);

    @Select("${sql}")
    Boolean tableCheckExist(@Param("sql") String sql);

    @Select("${sql}")
    List<DbTable> tableList(@Param("sql") String sql);

    @Select("${sql}")
    List<DbColumn> columnList(@Param("sql") String sql);

}