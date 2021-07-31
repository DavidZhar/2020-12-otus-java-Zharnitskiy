package ru.otus.container.core;

import ru.otus.container.annotation.PostConstruct;

import java.util.Arrays;

public class PostConstructBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcess(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredMethods()).filter(m -> m.isAnnotationPresent(PostConstruct.class))
                .forEach(m -> {
                    try {
                        m.setAccessible(true);
                        m.invoke(bean);
                    } catch (Exception e) {
                        throw new RuntimeException("Exception during bean initializing (PostConstruct): " + bean
                                .getClass());
                    }
                });
        return bean;
    }
}
