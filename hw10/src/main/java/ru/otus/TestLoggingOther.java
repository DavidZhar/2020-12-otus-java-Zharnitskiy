package ru.otus;

import ru.otus.proxy.TestInterface;

public class TestLoggingOther implements TestInterface {
    @Log
    @Override
    public void calculation(int param1) {
        calculation(param1, param1);
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        calculation(param1, param2, String.valueOf(param1 + param2));
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println(param1 + param2 + param3);
    }
}
