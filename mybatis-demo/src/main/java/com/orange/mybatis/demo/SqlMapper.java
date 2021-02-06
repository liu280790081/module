package com.orange.mybatis.demo;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface SqlMapper {

    @Select("select 'hello world !!!'")
    String helloWorld();

    @Select("select version()")
    String version();

    @Select("select * from onl_dict")
    <T> List<T> helloWorldNew();


    @Results(id = "groupWithUsers", value = {@Result(property = "userList", javaType = List.class, many = @Many(select = "selectUsersByGroupId"), column = "id")})
    //查询
    @Select({"select * from onl_table where 1=1 limit 10"})
    List<Map<String, Object>> selectGroupWithUsers();


    @Select({"select * from onl_table_field where table_id = #{tableId}"})
    List<Map<String, Object>> selectUsersByGroupId(@Param("tableId") String tableId);
}
