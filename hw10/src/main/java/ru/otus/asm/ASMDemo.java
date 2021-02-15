package ru.otus.asm;

import ru.otus.TestLogging;
import ru.otus.proxy.TestInterface;

public class ASMDemo {

    public static void main(String[] args) throws Exception {
        TestInterface test = ASMBuilder.get(TestLogging.class);
        test.calculation(6);
        test.calculation(6, 5);
        test.calculation(6, 5, " String");
    }
}
