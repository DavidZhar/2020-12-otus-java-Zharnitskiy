package ru.otus.container.model;

import ru.otus.container.PostConstructTest;
import ru.otus.container.annotation.PostConstruct;

public class ServiceBImpl implements ServiceB {

    @Override
    @PostConstruct
    public void methodForPostConstructTesting() {
        PostConstructTest.methodCheck = true;
    }
}
