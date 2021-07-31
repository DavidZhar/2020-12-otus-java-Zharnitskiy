package ru.otus.container.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.container.annotation.Aspect;
import ru.otus.container.core.BeanPostProcessor;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AspectBeanPostProcessor implements BeanPostProcessor {
    private final static Logger log = LoggerFactory.getLogger(AspectBeanPostProcessor.class);

    @Override
    public Object postProcess(Object bean) {
        Class<?> beanClass = bean.getClass();
        final Map<String, AspectDefinition> methods = new HashMap<>();

        if (beanClass.isAnnotationPresent(Aspect.class)) {
            Aspect annotation = beanClass.getAnnotation(Aspect.class);
            Arrays.stream(beanClass.getDeclaredMethods())
                    .forEach(m -> methods.put(m.getName() + Arrays.toString(m.getParameterTypes()),
                            getAspectDefinition(annotation)));
        }

        Arrays.stream(beanClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Aspect.class))
                .forEach(m -> {
                    Aspect annotation = m.getAnnotation(Aspect.class);
                    methods.put(m.getName() + Arrays.toString(m.getParameterTypes()),
                            getAspectDefinition(annotation));
                });

        if (methods.size() > 0) {
            return createProxyWithWrappedMethods(beanClass, bean, methods);
        }
        return bean;
    }

    private AspectDefinition getAspectDefinition(Aspect annotation) {
        return AspectDefinition.builder()
                .type(annotation.type())
                .aspect(annotation.clazz())
                .build();
    }

    private <T> T createProxyWithWrappedMethods(Class<T> beanClass, Object bean, Map<String, AspectDefinition> methods) {
        Object proxyInstance = Proxy
                .newProxyInstance(ClassLoader.getSystemClassLoader(), beanClass
                        .getInterfaces(), (proxy, method, args) -> {
                    String methodSignature = method.getName() + Arrays.toString(method.getParameterTypes());
                    if (methods.containsKey(methodSignature)) {
                        AspectDefinition aspectDefinition = methods.get(methodSignature);
                        CustomAspect aspect = AspectManager.getAspectByType(aspectDefinition.getAspect());
                        try {
                            if (aspectDefinition.getType() == AspectType.BEFORE) {
                                aspect.before();
                            }
                            Object result = method.invoke(bean, args);
                            if (aspectDefinition.getType() == AspectType.AFTER) {
                                aspect.after();
                            }
                            return result;
                        } catch (Exception e) {
                            log.error("Exception during aspects executing or method invocation", e);
                            throw new RuntimeException("Exception during aspects executing or method invocation", e);
                        }
                    } else {
                        return method.invoke(bean, args);
                    }
                });
        return (T) proxyInstance;
    }
}
