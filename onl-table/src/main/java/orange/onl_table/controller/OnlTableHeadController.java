package orange.onl_table.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping({"/table/head"})
public class OnlTableHeadController {


    @ApiOperation(value = "获取onl表集合", notes = "获取onl表集合")
    @PostMapping({"/list"})
    public String list(@RequestBody JSONObject param) {
        return "";
    }

}

