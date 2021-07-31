package ru.otus.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.container.config.AspectConfig;
import ru.otus.container.core.Context;
import ru.otus.container.core.ContextImpl;
import ru.otus.container.model.ServiceA;

public class AspectTest {
    public static boolean aspectCheck = false;
    public static boolean methodCheck = false;

    @BeforeEach
    void setFalse(){
        aspectCheck = false;
        methodCheck = false;
    }

    @Test
    void shouldExecuteAspectBefore(){
        Context context = new ContextImpl(AspectConfig.class);
        ServiceA bean = context.getBean(ServiceA.class);

        bean.methodForAspectTestingBefore();

        Assertions.assertTrue(aspectCheck);
        Assertions.assertTrue(methodCheck);
    }

    @Test
    void shouldExecuteAspectAfter(){
        Context context = new ContextImpl(AspectConfig.class);
        ServiceA bean = context.getBean(ServiceA.class);

        bean.methodForAspectTestingAfter();

        Assertions.assertTrue(aspectCheck);
        Assertions.assertTrue(methodCheck);
    }
}
