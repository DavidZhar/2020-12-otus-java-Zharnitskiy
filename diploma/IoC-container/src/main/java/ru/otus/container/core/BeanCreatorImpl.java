package ru.otus.container.core;

import ru.otus.container.annotation.Autowired;
import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Component;
import ru.otus.container.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanCreatorImpl implements BeanCreator {
    private final Context context;
    private final Map<Class<?>, Pair<Method, Class<?>>> beanCreateMethods;
    private final List<Class<?>> componentClasses;

    public BeanCreatorImpl(Context context, Map<Class<?>, Pair<Method, Class<?>>> beanCreateMethods,
                           List<Class<?>> componentClasses) {
        this.context = context;
        this.beanCreateMethods = beanCreateMethods;
        this.componentClasses = componentClasses;
    }

    public void createBeans() {
        beanCreateMethods.forEach((beanClass, pair) -> {
            Object configClassInstance = getConfigInstance(pair);
            Method method = pair.first;
            processMethod(configClassInstance, method);
        });
        componentClasses.forEach(this::processComponentClass);
    }

    private Object retrieveOrCreateBean(Class<?> beanType) {
        try {
            return context.getBean(beanType);
        } catch (Exception e) {
            if (beanCreateMethods.containsKey(beanType)) {
                Pair<Method, Class<?>> methodClassPair = beanCreateMethods.get(beanType);
                Method method = methodClassPair.first;
                return processMethod(getConfigInstance(methodClassPair), method);
            } else if (componentClasses.stream().anyMatch(beanType::isAssignableFrom)) {
                Class<?> beanClass = componentClasses.stream().filter(beanType::isAssignableFrom).findFirst().get();
                return processComponentClass(beanClass);
            } else throw new RuntimeException("Argument is not a bean: " + beanType.getName());
        }
    }

    private Object getConfigInstance(Pair<Method, Class<?>> pair) {
        try {
            return pair.second.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Config class should have default constructor! Class: " + pair.second.getName());
        }
    }

    private Object processMethod(Object configInstance, Method method) {
        Class<?> beanType = method.getReturnType();
        try {
            return context.getBean(beanType);
        } catch (Exception ignored) {
        }

        String name = method.getAnnotation(Bean.class).name();
        if (name.isEmpty()) name = beanType.getSimpleName();

        List<Class<?>> argsClasses = Arrays.asList(method.getParameterTypes());
        List<?> argsObjects = argsClasses.stream()
                .map(this::retrieveOrCreateBean)
                .collect(Collectors.toList());

        try {
            Object bean = method.invoke(configInstance, argsObjects.toArray());
            context.addBean(name, bean.getClass(), bean);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException("Check your config once again!", e);
        }
    }

    private Object processComponentClass(Class<?> beanType) {
        try {
            return context.getBean(beanType);
        } catch (Exception ignored) {
        }

        String name = beanType.getAnnotation(Component.class).name();
        if (name.isEmpty()) name = beanType.getSimpleName();

        List<Constructor<?>> constructors = Arrays.stream(beanType.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList());
        if (constructors.size() > 1)
            throw new RuntimeException("Only one constructor can be autowired! Component: " + beanType.getName());
        else if (constructors.size() == 1) {
            Constructor<?> constructor = constructors.get(0);
            Class<?>[] args = constructor.getParameterTypes();
            List<?> argsObjects = Arrays.stream(args)
                    .map(this::retrieveOrCreateBean)
                    .collect(Collectors.toList());

            try {
                Object bean = constructor.newInstance(argsObjects.toArray());
                context.addBean(name, beanType, bean);
                return bean;
            } catch (Exception e) {
                throw new RuntimeException("Check your config once again!", e);
            }
        } else {
            Field[] declaredFields = beanType.getDeclaredFields();
            Object bean;
            try {
                bean = beanType.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Component class should have default constructor or autowired constructor! Class: "
                        + beanType.getName());
            }
            Arrays.stream(declaredFields).filter(f -> f.isAnnotationPresent(Autowired.class)).forEach(f -> {
                Object value = retrieveOrCreateBean(f.getType());
                f.setAccessible(true);
                try {
                    f.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Something went wrong");
                }
            });
            return bean;
        }
    }

}
