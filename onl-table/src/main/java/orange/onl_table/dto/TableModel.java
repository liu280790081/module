package orange.onl_table.dto;


import lombok.Data;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.entity.OnlTableHead;
import orange.onl_table.entity.OnlTableIndex;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TableModel {

    @NotNull
    private OnlTableHead head;

    @NotEmpty
    private List<OnlTableField> fields;

    private List<OnlTableIndex> indices;

}
