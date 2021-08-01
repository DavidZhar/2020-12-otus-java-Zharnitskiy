package ru.otus.container.model;

import ru.otus.container.PostConstructTest;
import ru.otus.container.annotation.PostConstruct;

public class ServiceBImpl implements ServiceB {

    private final ServiceA serviceA;

    public ServiceBImpl(ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    @Override
    public ServiceA getServiceA() {
        return serviceA;
    }

    @Override
    @PostConstruct
    public void methodForPostConstructTesting() {
        PostConstructTest.methodCheck = true;
    }
}
