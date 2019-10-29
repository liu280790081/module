package test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import test.bean.KeyParam;
import test.bean.TestParam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class LambdaTest {

    private static List<TestParam> getTestList() {
        //分组
        TestParam a = new TestParam("1", "a1", 1);
        TestParam b = new TestParam("1", "b2", 2);
        TestParam c = new TestParam("3", "c3", 3);
        TestParam d = new TestParam("3", "d4", 4);
        TestParam e = new TestParam("5", "e5", 5);

        return Arrays.asList(a, b, c, d, e);
    }

    private static Stream getTestStream() {
        return getTestList().stream();
    }

    @Test
    public void testStream() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        String x = list.stream().filter(f -> f > 5).map(String::valueOf).collect(joining());
        System.out.println(x);

//        list.stream().collect(toCollection(ESTestParam::new));

//        Optional.ofNullable(123213)
        List newL = list.stream().peek(i -> {
            System.out.println(i);
            System.out.println(i + 10);
        }).collect(toList());
        System.out.println(newL);

        List<TestParam> esList = getTestList();
        Map<String, List<TestParam>> collect = esList.stream()
                .collect(groupingBy(TestParam::getUri));
        System.out.println(JSON.toJSONString(collect));

        Map<KeyParam, List<TestParam>> collects = esList.stream()
                .collect(groupingBy(k -> new KeyParam(k.getUri(), k.getMethod())));
        System.out.println(JSON.toJSONString(collects));

        Map<List<String>, List<TestParam>> collecta = esList.stream()
                .collect(groupingBy(k -> Arrays.asList(k.getUri(), k.getMethod())));
        System.out.println(JSON.toJSONString(collecta));

        Map<String, String> aaa = esList.stream()
                .collect(toConcurrentMap(TestParam::getUri, TestParam::getMethod));
        System.out.println(JSON.toJSONString(aaa));

        String collect2 = esList.stream()
                .map(TestParam::getMethod)
                .collect(joining(",", "[", "]"));
        System.out.println(collect2);

        String[] a1 = esList.stream()
                .map(TestParam::getMethod).toArray(String[]::new);
        System.out.println(JSON.toJSONString(a1));

//        esList.stream()

        //自定义
//        List<String> collecta = Stream.of("1", "2", "3").collect(
//                reducing(new ArrayList<String>(), x -> Arrays.asList(x), (y, z) -> {
//                    y.addAll(z);
//                    return y;
//                }));
//        System.out.println(collecta);
    }

    @Test
    public void reduceTest() {

        //累加，初始化值是 10
        Integer reduce = Stream.of(5, 2, 3, 4)
                .reduce(10, (count, item) -> {
                    System.out.println("count:" + count);
                    System.out.println("item:" + item);
                    return count + item;
                });
        System.out.println(reduce);

        String aa = Stream.of("fff", "111", "dddd").reduce("11", (a, b) -> a + b);
        System.out.println(aa);

//        Integer reduce1 = Stream.of(1, 2, 3, 4)
//                .reduce(0, (x, y) -> x + y);
//        System.out.println(reduce1);
//
//        String reduce2 = Stream.of("1", "2", "3")
//                .reduce("0", (x, y) -> (x + "," + y));
//        System.out.println(reduce2);
        Stream ss = Stream.concat(Stream.of(11, 222, 33), Stream.of("123"));
        List aaa = (List) ss.collect(Collectors.toList());
        System.out.println(aaa);
    }

    @Test
    public void testOptional() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("abc", "1111111111");

        TestParam test = new TestParam("123123", "macdfkkk", paramMap);

        boolean res = Optional.of(test.getParamMap()).map(map -> map.get("abc")).isPresent();
        System.out.println(res);


        Optional.of(test).map(t -> Optional.of(t.getUri()));

        TestParam aaa = Optional.ofNullable(test).orElse(new TestParam());


    }

    @Test
    public void testList() {
        int size = 10;
        List<Integer> list = new ArrayList<>(size);
        for (Integer i = 0; i < size; i++) {
            list.add(new Integer(i));
        }

        //同步
        long start1 = System.currentTimeMillis();
        List<Integer> temp3 = list.stream().collect(toList());
        System.out.println(System.currentTimeMillis() - start1);

        //并发
        long start2 = System.currentTimeMillis();
        List<Integer> temp2 = list.parallelStream().collect(toList());
        System.out.println(System.currentTimeMillis() - start2);

        List<Integer> temp1 = new ArrayList<>(size);
        //老的
        long start = System.currentTimeMillis();
        for (Integer i : list) {
            temp1.add(i);
        }
        System.out.println(System.currentTimeMillis() - start);


    }


    /**
     * @return
     * @throws FileNotFoundException 统计一篇文章的词频直方图。
     */
    @Test
    public void toMapForWord() {
        String address = "";
        Objects.requireNonNull(address, "The parameter cannot be empty");
        Pattern whitespace = Pattern.compile("\\s+");
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(address)))) {
            reader.lines()
                    .flatMap(whitespace::splitAsStream)
                    .collect(groupingBy(String::toLowerCase, counting()));
        } catch (Exception e) {
            // Error log
        }
    }

    @Test
    public void testStream2() {
        Stream.generate(() -> null);

        Stream.of("a", "b", "c", "d", "e")
                .skip(2)
                .forEach(System.out::println);

        getTestList().stream().sorted(new Comparator<TestParam>() {
            @Override
            public int compare(TestParam o1, TestParam o2) {
                return o2.getSort() - o1.getSort();
            }
        }).forEach(k -> System.out.println(JSON.toJSON(k)));

        System.out.println("----------------------------------------------");

        getTestList().stream().sorted(Comparator.comparing(new Function<TestParam, Integer>() {
            @Override
            public Integer apply(TestParam esTestParam) {
                return Integer.valueOf(esTestParam.getUri());
            }
        })).forEach(k -> System.out.println(JSON.toJSON(k)));

        System.out.println("----------------------------------------------");

        getTestList().stream().sorted(Comparator.comparingInt(new ToIntFunction<TestParam>() {
            @Override
            public int applyAsInt(TestParam value) {
                return -Integer.valueOf(value.getUri());
            }
        })).forEach(k -> System.out.println(JSON.toJSON(k)));
    }

    @Test
    public void testStream3() {
        Stream.of(null, 0L, 0).filter(Objects::nonNull).findAny().get();
    }

    @Test
    public void test3() {
        List<Map<String, Object>> oldList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", "val" + i);
            oldList.add(map);
        }

        System.out.println("1:\n\t" + JSON.toJSONString(oldList));
        String pkVal = "val2";
        oldList.removeIf(map -> Objects.equals(pkVal, map.get("key")));
        System.out.println("2:\n\t" + JSON.toJSONString(oldList));
    }


}
