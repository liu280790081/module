package test;

import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.internal.Engine;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BTest {

    @Test
    public void a() throws ParseException {
//        int x = 0;
//        for (int i = 0; i < 10; i++) {
//
//            System.out.println(x++);
//
//        }

//        Assert.assertSame();False();
//        Asserts.notBlank();

//        ObjectUtils.isNotEmpty()


        Map<String, Object> map = new HashMap<>();

        map.put("adfadsf", null);

        System.out.println(map.containsKey("adfadsf"));

        Date date = DateUtils.parseDate("20200101T061608Z", "yyyyMMdd'T'HHmmss'Z'");

        System.out.println(date);
        System.out.println(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.format(date));
        System.out.println(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(date));
        System.out.println(FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date));
    }

    private static JexlEngine jexlEngine = new Engine();

    public static Object executeExpression(String jexlExpression, Map<String, Object> map) {
        JexlExpression expression = jexlEngine.createExpression(jexlExpression);

        JexlContext context = new MapContext();
        if (!map.isEmpty()) {
            map.forEach(context::set);
        }
        return expression.evaluate(context);
    }

    @Test
    public void test2(){
        Map<String, Object> map = new HashMap<>();
        map.put("alive", "coding every day");
        map.put("out", System.out);
        String expression = "out.print(alive)";
        executeExpression(expression, map);
    }


    @Test
    public void test3(){

        // Create or retrieve an engine
        JexlEngine jexl = new JexlBuilder().create();

        // Create an expression
        String jexlExp = "foo.innerFoo.bar()";
        JexlExpression e = jexl.createExpression( jexlExp );


        // Create a context and add data
//        JexlContext jc = new MapContext();
//        jc.set("foo", new Foo() );

        // Now evaluate the expression, getting the result
//        Object o = e.evaluate(jc);

    }


    @Test
    public void test4() {


    }

}
