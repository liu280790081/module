package orange.onl_table.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ido85.icip.system.table.dto.DbColumn;
import com.ido85.icip.system.table.dto.DbTable;
import com.ido85.icip.system.table.entity.OnlTableHead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OnlTableHeadMapper extends BaseMapper<OnlTableHead> {

    void executeDDL(@Param("sqlStr") String var1);

    List<Map<String, Object>> queryList(@Param("sqlStr") String var1);

    List<OnlTableHead> getContainFKScheduleList(@Param("pkTableId") String pkTableId);

    /* ------------------------------数据库实体表------------------------------------ */

    void dropDBTable(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    List<Map<String, String>> getDBTableList(@Param("tableSchema") String tableSchema, @Param("keyword") String keyword);

    DbTable getDBTableInfo(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    List<DbColumn> getDBTableColumnList(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    Boolean checkDBTableExist(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    /* ------------------------------数据库实体表数据------------------------------------ */

    int deleteDBTableData(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName,
                          @Param("pkParam") Map<String, Object> pkParam);

    long getDBTableExist(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName,
                         @Param("pkParam") Map<String, Object> pkParam);

    Map<String, Object> getDBTableData(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName,
                                       @Param("pkParam") Map<String, Object> pkParam);

    List<Map<String, Object>> getDBTableDataList(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName,
                                                 @Param("fkParam") Map<String, Object> fkParam);

    IPage<Map<String, Object>> getDBTableDataPage(IPage page, @Param("tableSchema") String tableSchema,
                                                  @Param("tableName") String tableName,
                                                  @Param("fkParam") Map<String, Object> fkParam);
}