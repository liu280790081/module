package test.java8;

import java.util.function.Supplier;

public class QuoteTest {

    //Supplier是jdk1.8的接口，这里和lamda一起使用了
    public static QuoteTest create(final Supplier<QuoteTest> supplier) {
        return supplier.get();
    }

    public static void collide(final QuoteTest car) {
        System.out.println("Collided " + car.toString());
    }

    public void follow(final QuoteTest another) {
        System.out.println("Following the " + another.toString());
    }

    public void repair() {
        System.out.println("Repaired " + this.toString());
    }
}
