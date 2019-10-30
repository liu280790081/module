package orange.onl_table.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ido85.icip.system.table.dao.OnlTableHeadMapper;
import com.ido85.icip.system.table.dao.OnlTableIndexMapper;
import com.ido85.icip.system.table.entity.OnlTableIndex;
import com.ido85.icip.system.table.service.OnlTableIndexService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;


@Service
public class OnlTableIndexServiceImpl extends ServiceImpl<OnlTableIndexMapper, OnlTableIndex> implements OnlTableIndexService {
    @Resource
    private OnlTableHeadMapper formHeadMapper;

    public void createIndex(String code, String databaseType, String tbname) {
        LambdaQueryWrapper<OnlTableIndex> var4 = new LambdaQueryWrapper<>();
        var4.eq(OnlTableIndex::getHeadId, code);
        List var5 = this.list(var4);
        String var8;
        if (var5 != null && var5.size() > 0) {
            for (Iterator var6 = var5.iterator(); var6.hasNext(); this.formHeadMapper.executeDDL(var8)) {
                OnlTableIndex var7 = (OnlTableIndex) var6.next();
                String var9 = var7.getIndexName();
                String var10 = var7.getIndexField();
                String var11 = "normal".equals(var7.getIndexType()) ? " index " : var7.getIndexType() + " index ";
                byte var13 = -1;
                switch (databaseType.hashCode()) {
                    case -1955532418:
                        if (databaseType.equals("ORACLE")) {
                            var13 = 1;
                        }
                        break;
                    case -1620389036:
                        if (databaseType.equals("POSTGRESQL")) {
                            var13 = 3;
                        }
                        break;
                    case 73844866:
                        if (databaseType.equals("MYSQL")) {
                            var13 = 0;
                        }
                        break;
                    case 912124529:
                        if (databaseType.equals("SQLSERVER")) {
                            var13 = 2;
                        }
                }

                switch (var13) {
                    case 0:
                        var8 = "create " + var11 + var9 + " on " + tbname + "(" + var10 + ")";
                        break;
                    case 1:
                        var8 = "create " + var11 + var9 + " on " + tbname + "(" + var10 + ")";
                        break;
                    case 2:
                        var8 = "create " + var11 + var9 + " on " + tbname + "(" + var10 + ")";
                        break;
                    case 3:
                        var8 = "create " + var11 + var9 + " on " + tbname + "(" + var10 + ")";
                        break;
                    default:
                        var8 = "create " + var11 + var9 + " on " + tbname + "(" + var10 + ")";
                }
            }
        }

    }
}
