package ru.otus.container.model;

import ru.otus.container.AspectTest;
import ru.otus.container.annotation.Aspect;
import ru.otus.container.aop.AspectType;
import ru.otus.container.config.TestAspect;

public class ServiceCImpl implements ServiceC {

    @Override
    @Aspect(clazz = TestAspect.class, type = AspectType.BEFORE)
    public void methodForAspectTestingBefore() {
        AspectTest.methodCheck = true;
    }

    @Override
    @Aspect(clazz = TestAspect.class, type = AspectType.AFTER)
    public void methodForAspectTestingAfter() {
        AspectTest.methodCheck = true;
    }
}
