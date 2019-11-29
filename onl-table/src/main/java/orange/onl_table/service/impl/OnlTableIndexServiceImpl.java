package orange.onl_table.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import orange.onl_table.common.config.DataBaseConfig;
import orange.onl_table.common.db.IDbHandler;
import orange.onl_table.dao.OnlTableHeadMapper;
import orange.onl_table.dao.OnlTableIndexMapper;
import orange.onl_table.entity.OnlTableIndex;
import orange.onl_table.service.OnlTableIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;


@Service
public class OnlTableIndexServiceImpl extends ServiceImpl<OnlTableIndexMapper, OnlTableIndex> implements OnlTableIndexService {

    @Autowired
    private DataBaseConfig dataBaseConfig;
    @Resource
    private OnlTableHeadMapper formHeadMapper;

    public void createIndex(String code, String tbName) {
        LambdaQueryWrapper<OnlTableIndex> var4 = new LambdaQueryWrapper<>();
        var4.eq(OnlTableIndex::getHeadId, code);
        List var5 = this.list(var4);
        String var8;
        IDbHandler handler = IDbHandler.dbHandler(dataBaseConfig.getDbName());
        if (var5 != null && var5.size() > 0) {
            for (Iterator var6 = var5.iterator(); var6.hasNext(); this.formHeadMapper.executeDDL(var8)) {
                OnlTableIndex var7 = (OnlTableIndex) var6.next();
                var8 = handler.indexCreateSql(tbName, var7);
            }
        }

    }
}
