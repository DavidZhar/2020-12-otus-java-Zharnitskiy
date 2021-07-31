package ru.otus.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.container.config.ComponentConfig;
import ru.otus.container.core.Context;
import ru.otus.container.core.ContextImpl;
import ru.otus.container.model.ServiceA;
import ru.otus.container.model.components.ServiceAComponent;
import ru.otus.container.model.components.ServiceBComponent;

public class ComponentTest {

    @Test
    void shouldExecuteAspectBefore() {
        Context context = new ContextImpl(ComponentConfig.class);
        ServiceBComponent serviceBComponent = context.getBean(ServiceBComponent.class);
        ServiceAComponent serviceAComponent = context.getBean(ServiceAComponent.class);
        ServiceA serviceA = context.getBean(ServiceA.class);

        Assertions.assertEquals(serviceAComponent, serviceBComponent.getServiceAComponent());
        Assertions.assertEquals(serviceA, serviceAComponent.getServiceA());
    }
}
