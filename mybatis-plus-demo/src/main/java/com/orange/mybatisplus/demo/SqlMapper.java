package com.orange.mybatisplus.demo;

import org.apache.ibatis.annotations.Select;

public interface SqlMapper {

    @Select("select 'hello world !!!'")
    String helloWorld();

    @Select("select version()")
    String version();



}
