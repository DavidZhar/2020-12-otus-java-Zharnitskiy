package ru.otus.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.container.config.ContainerConfig;
import ru.otus.container.core.Context;
import ru.otus.container.core.ContextImpl;
import ru.otus.container.model.ServiceA;
import ru.otus.container.model.ServiceB;

public class ContainerTest {

    @Test
    void shouldExecuteAspectBefore() {
        Context context = new ContextImpl(ContainerConfig.class);
        ServiceA serviceA = context.getBean(ServiceA.class);
        ServiceB serviceB = context.getBean(ServiceB.class);
        ServiceB serviceBByName = context.getBean("serviceBCustomName");

        Assertions.assertEquals(serviceB.getServiceA(), serviceA);
        Assertions.assertEquals(serviceB, serviceBByName);
    }
}
