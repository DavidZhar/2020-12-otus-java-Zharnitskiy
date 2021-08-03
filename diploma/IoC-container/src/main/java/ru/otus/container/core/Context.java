package ru.otus.container.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public interface Context {
    <C> C getBean(Class<C> beanClass);

    <C> C getBean(String beanName);

    Map<String, Object> getAllBeans();

    void addBean(String beanName, Class<?> beanClass, Object bean);

    void addComponentClasses(Set<Class<?>> classes);

    void addBeanCreateMethod(Method m, Class<?> configClass);

}
