package orange.onl_table.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ido85.icip.system.table.entity.OnlTableIndex;

public interface OnlTableIndexService extends IService<OnlTableIndex> {

    void createIndex(String var1, String var2, String var3);
}
