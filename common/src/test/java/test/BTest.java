package test;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.util.Asserts;
import org.junit.Assert;
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




    @Test
    private void test2(){
//        JexlEngine jexl=new JexlEngine();
//        Expression e = jexl.createExpression(jexlExp);
//        JexlContext jc = new MapContext();
//        for(String key:map.keySet()){
//            jc.set(key, map.get(key));
//        }
//        if(null==e.evaluate(jc)){
//            return "";
//        }
//        return e.evaluate(jc);


    }

}
