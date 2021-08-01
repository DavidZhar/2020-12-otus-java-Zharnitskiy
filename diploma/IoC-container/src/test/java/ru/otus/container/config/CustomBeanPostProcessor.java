package ru.otus.container.config;

import ru.otus.container.CustomBeanPostProcessorTest;
import ru.otus.container.core.BeanPostProcessor;

public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcess(Object bean) {
        CustomBeanPostProcessorTest.postProcessCheck = true;
        return bean;
    }
}
