package orange.onl_table.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import orange.onl_table.entity.OnlTableHead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OnlTableHeadMapper extends BaseMapper<OnlTableHead> {

    @Update("${sql}")
    void executeDDL(@Param("sql") String sql);

    @Select("select h.* " +
            "from onl_table_head h " +
            "join onl_table_field f on f.head_id = h.id " +
            "where main_table =  (select table_name from onl_table_head where id = #{pkTableId})")
    List<OnlTableHead> getContainFKScheduleList(@Param("pkTableId") String pkTableId);

}