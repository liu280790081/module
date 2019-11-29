package orange.onl_table.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import orange.onl_table.entity.OnlTableHead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OnlTableHeadMapper extends BaseMapper<OnlTableHead> {

    void executeDDL(@Param("sqlStr") String var1);

    List<OnlTableHead> getContainFKScheduleList(@Param("pkTableId") String pkTableId);

}