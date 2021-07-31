package ru.otus.container.config;

import ru.otus.container.AspectTest;
import ru.otus.container.aop.CustomAspect;

public class TestAspect implements CustomAspect {
    @Override
    public void before() {
        AspectTest.aspectCheck = true;
    }

    @Override
    public void after() {
        AspectTest.aspectCheck = true;
    }
}
