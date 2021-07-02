package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.AppConfig1;
import ru.otus.config.AppConfig2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        processConfigClasses(configClasses);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner(), new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        processConfigClasses(classes.toArray(Class[]::new));
    }

    private void processConfigClasses(Class<?>[] configClasses) {
        Arrays.stream(configClasses).peek(this::checkConfigClass)
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        try {
            Object configInstance = configClass.newInstance();
            List<Method> methods = Arrays.stream(configClass.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(AppComponent.class))
                    .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                    .collect(Collectors.toList());
            methods.forEach(m -> processMethod(configInstance, m));
        } catch (Exception e) {
            throw new RuntimeException("Check your config once again!", e);
        }
    }

    private void processMethod(Object configInstance, Method method) {
        String name = method.getAnnotation(AppComponent.class).name();
        List<Class<?>> argsClasses = Arrays.asList(method.getParameterTypes());
        List<?> argsObjects = argsClasses.stream().map(this::getAppComponent).collect(Collectors.toList());
        try {
            Object component = method.invoke(configInstance, argsObjects.toArray());
            appComponents.add(component);
            appComponentsByName.put(name, component);
        } catch (Exception e) {
            throw new RuntimeException("Check your config once again!", e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream().filter(c -> componentClass.isAssignableFrom(c.getClass())).findFirst()
                .orElseThrow(() -> new RuntimeException("No such component!"));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        try {
            C component = (C) appComponentsByName.get(componentName);
            return component;
        } catch (Exception e) {
            throw new RuntimeException("No such component!");
        }
    }
}
