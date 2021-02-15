package ru.otus.proxy;

import ru.otus.TestLogging;
import ru.otus.TestLoggingOther;

public class ProxyDemo {

    public static void main(String[] args) {
        TestInterface test = AOPDemoIoC.get(TestLogging.class);
        test.calculation(6);
        test.calculation(6, 5);
        test.calculation(6, 5, " String");

        System.out.println("---------------------------------------------");
        System.out.println("Example that log annotation works only once when using internal methods:");

        TestInterface testOther = AOPDemoIoC.get(TestLoggingOther.class);
        testOther.calculation(6);
    }

}
