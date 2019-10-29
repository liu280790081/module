package test;

import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.function.Function;

public class ATest {

    @Test
    public void testPatternUncorrect() {
        String UPDATE_SQL = "update t_handle_time a set a.start_time=sysdate where table_name = 'dynamic_{0}'";

        Assert.assertEquals("update t_handle_time a set a.start_time=sysdate where table_name = dynamic_{0}",
                MessageFormat.format(UPDATE_SQL, "test"));
    }

    @Test
    public void testPatternCorrect() {
        String UPDATE_SQL = "update t_handle_time a set a.start_time=sysdate where table_name = ''dynamic_{0}''";

        Assert.assertEquals("update t_handle_time a set a.start_time=sysdate where table_name = 'dynamic_test'",
                MessageFormat.format(UPDATE_SQL, "test"));
    }


    @Test
    public void aaaaaaaa() {
        String a = "jdbc:mysql://47.104.96.20:3306/icip?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useTimezone=true&serverTimezone=GMT%2B8";

        System.out.println(a.substring(a.lastIndexOf("/") + 1, a.lastIndexOf("?")));



        Function fun = new Function() {
            @Override
            public Object apply(Object o) {
                return null;
            }
        };


        Function<Integer, String> name = integer -> String.valueOf(integer + 2);

        Function<String, Integer> square = s -> Integer.valueOf(s + s);


        int value = name.andThen(square).apply(3);
        System.out.println("andThen value=" + value);
        String value2 = name.compose(square).apply("22");
        System.out.println("compose value2=" + value2);
        //返回一个执行了apply()方法之后只会返回输入参数的函数对象
        Object identity = Function.identity().apply("huohuo");
        System.out.println(identity);
    }

}
