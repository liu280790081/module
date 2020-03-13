package test.java8;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;

public class MapsTest {

    private static Map<String, Object> map;

    static {
        map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i + "", "val" + i);
        }
        map.forEach((id, val) -> System.out.println(val));
    }


    @Test
    public void test1() {
        map.computeIfPresent("3", (num, val) -> val + num);
        map.get("3");             // val33

        map.computeIfPresent("9", (num, val) -> null);
        map.containsKey("9");     // false

        map.computeIfAbsent("23", new Function<String, Object>() {
            @Override
            public Object apply(String s) {
                return null;
            }
        });
        map.containsKey("23");    // true

        map.computeIfAbsent("3", num -> "bam");
        map.get("3");

        System.out.println(JSON.toJSONString(map));


    }


    @Test
    public void test2() {
        Map<String, List<String>> maps = new HashMap<>();

        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        maps.merge("abc", list, (value, newValue) -> {
            value.addAll(newValue);
            return value;
        });
        System.out.println(JSON.toJSONString(maps));
    }

}
