package orange.onl_table.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ido85.icip.system.table.entity.OnlTableField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OnlTableFieldMapper extends BaseMapper<OnlTableField> {

    List<Map<String, Object>> queryListBySql(@Param("sqlStr") String var1);

    Integer queryCountBySql(@Param("sqlStr") String var1);

    void deleteAutoList(@Param("sqlStr") String var1);

    void editFormData(@Param("sqlStr") String var1);

    Map<String, Object> queryFormData(@Param("sqlStr") String var1);

    List<Map<String, Object>> queryListData(@Param("sqlStr") String var1);

    IPage<Map<String, Object>> selectPageBySql(Page var1, @Param("sqlStr") String var2);

    void executeInsertSQL(Map<String, Object> var1);

    void executeUpdateSQL(Map<String, Object> var1);

}
