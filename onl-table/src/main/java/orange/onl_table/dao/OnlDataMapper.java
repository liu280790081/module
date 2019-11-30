package orange.onl_table.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface OnlDataMapper {

    @Select("${sql}")
    Map<String, Object> executeOneSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    @Select("${sql}")
    Long executeCountSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    @Select("${sql}")
    List<Map<String, Object>> executeListSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    @Select("${sql}")
    IPage<Map<String, Object>> executePageSQL(IPage page, @Param("sql") String sql, @Param("param") Map<String, Object> param);

    @Insert("${sql}")
    int executeInsertSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    @Update("${sql}")
    int executeUpdateSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    @Delete("${sql}")
    int executeDeleteSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

}
