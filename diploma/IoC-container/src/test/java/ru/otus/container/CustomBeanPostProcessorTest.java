package ru.otus.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.container.config.CustomBeanPostProcessorConfig;
import ru.otus.container.core.ContextImpl;

public class CustomBeanPostProcessorTest {
    public static boolean postProcessCheck = false;

    @Test
    void shouldExecutePostProcess() {
        new ContextImpl(CustomBeanPostProcessorConfig.class);

        Assertions.assertTrue(postProcessCheck);
    }
}
