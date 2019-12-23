package test;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class ReflexTest {

    @Test
    public void propertiesTest() {
        try {
            //public方式使用 getMethod()
            Method usernameTest = ReflexTest.class.getDeclaredMethod("usernameTest", String.class);
            Parameter[] parameters = usernameTest.getParameters();
            for (Parameter parameter : parameters) {
                System.out.println(parameter.getName());
            }
        } catch (NoSuchMethodException nme) {
            System.out.println(nme);
        }

    }

    private String usernameTest(String username) {
        return username;
    }


    String info = null;//成员变量

    void doWork(String name)     {
        System.out.println(x);
        int age;//局部变量
        if (true) {
            int num;//局部变量
        }
    }

    {
        int num;//局部变量
    }

    String x;











    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        try {
            Class c = Class.forName(ReflexTest.class.getName());
            //获取类的属性
            Field[] fields = c.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                System.out.println("类的属性有："+ Modifier.toString(fields[i].getModifiers())+"   "+fields[i].getType()+"    "+fields[i].getName());
            }
            //获取类的方法
            Method[] methods= c.getMethods();
            for (int j = 0; j <methods.length ; j++) {
                System.out.println("类的方法有："+Modifier.toString(methods[j].getModifiers())+"   "+methods[j].getReturnType()+"  "+methods[j].getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }





















}
