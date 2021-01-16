package ru.otus;

import com.google.common.base.Strings;

public class HelloOtus {
    public static void main(String[] args) {
        String hello = Strings.repeat("Hello Otus!\n", 10);
        System.out.println(hello);
    }
}
