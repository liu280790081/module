package orange.onl_table.dto;



import com.ido85.icip.system.table.entity.OnlTableField;
import com.ido85.icip.system.table.entity.OnlTableHead;
import com.ido85.icip.system.table.entity.OnlTableIndex;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TableModel {

    @NotNull
    private OnlTableHead head;

    @NotEmpty
    private List<OnlTableField> fields;

    private List<OnlTableIndex> indexs;

}
