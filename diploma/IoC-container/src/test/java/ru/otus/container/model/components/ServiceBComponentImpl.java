package ru.otus.container.model.components;

import ru.otus.container.annotation.Autowired;
import ru.otus.container.annotation.Component;

@Component
public class ServiceBComponentImpl implements ServiceBComponent {

    @Autowired
    private ServiceAComponent serviceAComponent;

    @Override
    public ServiceAComponent getServiceAComponent() {
        return serviceAComponent;
    }
}
