package ru.otus.container.model;

public interface ServiceB {
    ServiceA getServiceA();

    void methodForPostConstructTesting();
}
