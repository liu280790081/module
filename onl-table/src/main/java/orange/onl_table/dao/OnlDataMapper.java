package orange.onl_table.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OnlDataMapper {

    Map<String, Object> executeOneSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    Long executeCountSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    List<Map<String, Object>> executeListSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    IPage<Map<String, Object>> executePageSQL(IPage page, @Param("sql") String sql, @Param("param") Map<String, Object> param);

    int executeInsertSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    int executeUpdateSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    int executeDeleteSQL(@Param("sql") String sql, @Param("param") Map<String, Object> param);

}
