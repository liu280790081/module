package orange.onl_table.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import orange.onl_table.entity.OnlTableIndex;
import orange.onl_table.service.OnlTableIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping({"/table/index"})
public class OnlTableIndexController {

    @Autowired
    private OnlTableIndexService indexService;

    @GetMapping({"/listByHeadId/{code}"})
    public List a(@PathVariable("code") String var1) {
        QueryWrapper var2 = new QueryWrapper();
        var2.eq("head_id", var1);
        var2.orderByDesc("create_time");
        return this.indexService.list(var2);
    }

    @GetMapping({"/list"})
    public IPage a(@RequestBody JSONObject param, OnlTableIndex var1) {
        Integer var2 = Optional.ofNullable(param.getInteger("pageNo")).orElse(1);
        Integer var3 = Optional.ofNullable(param.getInteger("pageSize")).orElse(10);
        Page var7 = new Page(var2, var3);
        return this.indexService.page(var7);
    }

}

