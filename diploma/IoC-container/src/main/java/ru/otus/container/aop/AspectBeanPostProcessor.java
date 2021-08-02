package ru.otus.container.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.container.annotation.Aspect;
import ru.otus.container.core.BeanPostProcessor;
import ru.otus.container.core.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AspectBeanPostProcessor implements BeanPostProcessor {
    private final static Logger log = LoggerFactory.getLogger(AspectBeanPostProcessor.class);

    private final Context context;

    public AspectBeanPostProcessor(Context context) {
        this.context = context;
    }

    @Override
    public Object postProcess(Object bean) {
        Class<?> beanClass = bean.getClass();
        final Map<String, AspectDefinition> methods = new HashMap<>();

        if (beanClass.isAnnotationPresent(Aspect.class)) {
            Aspect annotation = beanClass.getAnnotation(Aspect.class);
            methods.putAll(Arrays.stream(beanClass.getDeclaredMethods())
                    .collect(Collectors.toMap(this::getMethodSignature, m -> getAspectDefinition(annotation))));
        }

        methods.putAll(Arrays.stream(beanClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Aspect.class))
                .collect(Collectors.toMap(this::getMethodSignature,
                        m -> getAspectDefinition(m.getAnnotation(Aspect.class)))));

        if (!methods.isEmpty()) {
            return createProxyWithWrappedMethods(beanClass, bean, methods);
        }
        return bean;
    }

    private String getMethodSignature(Method m) {
        return m.getName() + Arrays.toString(m.getParameterTypes());
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
                    String methodSignature = getMethodSignature(method);
                    if (methods.containsKey(methodSignature)) {
                        AspectDefinition aspectDefinition = methods.get(methodSignature);
                        CustomAspect aspect = null;
                        try {
                            aspect = (CustomAspect) context.getBean(aspectDefinition.getAspect());
                        } catch (Exception e) {
                            throw new RuntimeException("Aspect should implement CustomAspect and be declared as a bean", e);
                        }
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
