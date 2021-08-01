package ru.otus.container.transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.container.annotation.Transactional;
import ru.otus.container.core.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HibernateTransactionalBeanPostProcessor implements BeanPostProcessor {

    private final static Logger log = LoggerFactory.getLogger(HibernateTransactionalBeanPostProcessor.class);

    private final TransactionManager transactionManager;

    public HibernateTransactionalBeanPostProcessor(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object postProcess(Object bean) {
        Class<?> beanClass = bean.getClass();
        final Map<String, TransactionObject> methods = new HashMap<>();

        if (beanClass.isAnnotationPresent(Transactional.class)) {
            Transactional annotation = beanClass.getAnnotation(Transactional.class);
            Arrays.stream(beanClass.getDeclaredMethods())
                    .forEach(m -> methods.put(getMethodSignature(m),
                            getTransactionObject(annotation)));
        }

        Arrays.stream(beanClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Transactional.class))
                .forEach(m -> {
                    Transactional annotation = m.getAnnotation(Transactional.class);
                    methods.put(getMethodSignature(m),
                            getTransactionObject(annotation));
                });

        if (methods.size() > 0) {
            return createProxyWithWrappedMethods(beanClass, bean, methods);
        }
        return bean;
    }

    private TransactionObject getTransactionObject(Transactional annotation) {
        return TransactionObject.builder()
                .type(TransactionType.HIBERNATE)
                .propagation(annotation.propagation())
                .isolation(annotation.isolation())
                .build();
    }

    public <T> T createProxyWithWrappedMethods(Class<T> clazz, Object bean, Map<String, TransactionObject> methods) {
        Object proxyInstance = Proxy
                .newProxyInstance(ClassLoader.getSystemClassLoader(), clazz.getInterfaces(), (proxy, method, args) -> {
                    String methodSignature = getMethodSignature(method);
                    if (methods.containsKey(methodSignature)) {
                        try {
                            return transactionManager.doInTransaction(methods.get(methodSignature),
                                    () -> method.invoke(bean, args));
                        } catch (Exception e) {
                            log.error("Exception during transaction executing", e);
                            throw new DataBaseOperationException("Exception during transaction executing", e);
                        }
                    } else {
                        return method.invoke(bean, args);
                    }
                });
        return (T) proxyInstance;
    }

    private String getMethodSignature(Method method) {
        return method.getName() + Arrays.toString(method.getParameterTypes());
    }
}
