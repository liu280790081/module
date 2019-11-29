package orange.onl_table.service;

import com.baomidou.mybatisplus.extension.service.IService;
import orange.onl_table.entity.OnlTableIndex;

public interface OnlTableIndexService extends IService<OnlTableIndex> {

    void createIndex(String var1, String var3);
}
