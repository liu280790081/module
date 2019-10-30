package orange.onl_table.onl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import orange.onl_table.onl.entity.OnlTableIndex;

public interface OnlTableIndexService extends IService<OnlTableIndex> {

    void createIndex(String var1, String var2, String var3);
}
