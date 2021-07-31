package ru.otus.container.model.components;

import ru.otus.container.annotation.Autowired;
import ru.otus.container.annotation.Component;
import ru.otus.container.model.ServiceA;

@Component
public class ServiceAComponentImpl implements ServiceAComponent {

    private final ServiceA serviceA;

    @Override
    public ServiceA getServiceA() {
        return serviceA;
    }

    @Autowired
    public ServiceAComponentImpl(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
