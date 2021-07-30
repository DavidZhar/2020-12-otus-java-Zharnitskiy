package ru.otus.container.core;

public interface BeanPostProcessor {

    default Object postProcess(Object bean) {
        return bean;
    }

}
