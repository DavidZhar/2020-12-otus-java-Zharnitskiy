package ru.otus.container.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.container.service.ServiceA;
import ru.otus.container.service.ServiceAImpl;
import ru.otus.container.service.ServiceAImplAnother;

public class ContextImplTest {

    @Test
    void shouldReturnUpdatedBean() {
        Context context = new ContextImpl(new String[]{});

        context.addBean("serviceA", ServiceAImpl.class, new ServiceAImpl());
        context.addBean("serviceA", ServiceAImpl.class, new ServiceAImplAnother());

        Assertions.assertEquals(context.getBean("serviceA").getClass(), ServiceAImplAnother.class);
        Assertions.assertEquals(context.getBean(ServiceA.class).getClass(), ServiceAImplAnother.class);
    }
}
