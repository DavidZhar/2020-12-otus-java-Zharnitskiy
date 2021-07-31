package ru.otus.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.container.config.PostConstructConfig;
import ru.otus.container.core.ContextImpl;

public class PostConstructTest {
    public static boolean methodCheck = false;

    @Test
    void shouldExecutePostConstruct() {
        new ContextImpl(PostConstructConfig.class);

        Assertions.assertTrue(methodCheck);
    }
}
