package orange.onl_table.service;


import com.baomidou.mybatisplus.extension.service.IService;
import orange.onl_table.common.exception.OnlException;
import orange.onl_table.dto.TableModel;
import orange.onl_table.entity.OnlTableHead;

import java.sql.SQLException;
import java.util.List;

public interface OnlTableHeadService extends IService<OnlTableHead> {

    boolean addAll(TableModel var1);

    boolean editAll(TableModel var1);

    void doDbSync(String var1, String var2) throws Exception;

    void doDbSyncAll() throws Exception;

    void deleteRecordAndTable(List<String> var1, boolean isDelete) throws OnlException, SQLException;

    void dbTableToOnl(String... tableNames) throws Exception;

    void dbTableToOnl(String var1) throws Exception;

    List<OnlTableHead> queryContainFKScheduleList(String pkTableId);

    /**
     * 根据表名获取onl表信息
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    OnlTableHead queryByTableName(String tableName) throws Exception;

    OnlTableHead requireOne(String headId);
}

