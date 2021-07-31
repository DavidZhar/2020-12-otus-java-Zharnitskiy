//package ru.otus.container.transactional;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import ru.otus.container.annotation.Transactional;
//import ru.otus.container.core.BeanPostProcessor;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class QuiteSimpleHibernateTransactionalBeanPostProcessor implements BeanPostProcessor {
//
//    private final TransactionManager transactionManager;
//
//    public QuiteSimpleHibernateTransactionalBeanPostProcessor(TransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }
//
//    @Override
//    public Object postProcess(Object bean) {
//        Class<?> beanClass = bean.getClass();
//        List<Method> methods;
//        if (beanClass.isAnnotationPresent(Transactional.class)) {
//            methods = Arrays.asList(beanClass.getDeclaredMethods());
//        } else {
//            methods = Arrays.stream(beanClass.getDeclaredMethods())
//                    .filter(m -> m.isAnnotationPresent(Transactional.class)).collect(Collectors.toList());
//        }
//        if (methods.size() > 0) {
//            return createProxyWithWrappedMethods(beanClass, bean,
//                    methods.stream().map(m -> m.getName() + Arrays.toString(m.getParameterTypes()))
//                            .collect(Collectors.toList()));
//        }
//        return bean;
//    }
//
//    public <T> T createProxyWithWrappedMethods(Class<T> clazz, Object bean, List<String> methodNames) {
//        return (T) Proxy
//                .newProxyInstance(ClassLoader.getSystemClassLoader(), clazz.getInterfaces(), (proxy, method, args) -> {
//                    if (methodNames.contains(method.getName() + Arrays.toString(method.getParameterTypes()))) {
//                        return transactionManager.doInTransaction(() -> {
//                            try {
//                                return method.invoke(bean, args);
//                            } catch (Exception e) {
//                                throw new RuntimeException();
//                            }
//                        });
//                    }
//                    return method.invoke(bean, args);
//                });
//    }
//}
