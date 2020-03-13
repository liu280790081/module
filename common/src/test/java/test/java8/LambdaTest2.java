package test.java8;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import test.bean.TestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaTest2 {

    private static List<TestParam> testList;

    static {
        //分组
        TestParam a = new TestParam("1", "a1", 1);
        TestParam b = new TestParam("1", "b2", 2);
        TestParam c = new TestParam("3", "c3", 3);
        TestParam d = new TestParam("3", "d4", 4);
        TestParam e = new TestParam("5", "e5", 5);
        testList = new ArrayList<>(Arrays.asList(a, b, c, d, e));
    }

    @Test
    public void test() {
        testList.stream().map(new Function<TestParam, JSONObject>() {
            @Override
            public JSONObject apply(TestParam testParam) {
                return new JSONObject();
            }

            @Override
            public <V> Function<V, JSONObject> compose(Function<? super V, ? extends TestParam> before) {
                return null;
            }

            @Override
            public <V> Function<TestParam, V> andThen(Function<? super JSONObject, ? extends V> after) {
                return null;
            }
        }).collect(Collectors.toList());


        testList.stream().peek(new Consumer<TestParam>() {
            @Override
            public void accept(TestParam testParam) {

            }
        });


//        testList.stream().findFirst();
//        testList.stream()

//        Stream.iterate()

        List<JSONObject> list = testList.stream().map(var -> (JSONObject)JSON.toJSON(var)).collect(Collectors.toList());
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
        System.out.println(JSON.toJSONString(list));
    }
}
