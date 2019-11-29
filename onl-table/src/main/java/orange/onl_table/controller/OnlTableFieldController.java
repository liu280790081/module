package orange.onl_table.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import orange.onl_table.entity.OnlTableField;
import orange.onl_table.service.OnlTableFieldService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"/table/field"})
public class OnlTableFieldController {

    @Autowired
    private OnlTableFieldService fieldService;


    @ApiOperation(value = "获取表字段", notes = "获取表字段")
    @GetMapping({"/listByHeadId/{headId}"})
    public List listByHeadId(@PathVariable("headId") String var1) {
        LambdaQueryWrapper<OnlTableField> var2 = new LambdaQueryWrapper<>();
        var2.eq(OnlTableField::getHeadId, var1);
        var2.orderByAsc(OnlTableField::getOrderNum, OnlTableField::getCreateTime);
        return fieldService.list(var2);
    }


}
