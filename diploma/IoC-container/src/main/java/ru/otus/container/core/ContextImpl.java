package ru.otus.container.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.container.util.Pair;

import java.lang.reflect.Method;
import java.util.*;

public class ContextImpl implements Context {
    private final static Logger log = LoggerFactory.getLogger(ContextImpl.class);

    private final Map<Class<?>, Object> beansByType = new HashMap<>();
    private final Map<String, Object> beansByName = new HashMap<>();
    private final List<Class<?>> configClasses = new ArrayList<>();
    private final Map<Class<?>, Pair<Method, Class<?>>> beanCreateMethods = new HashMap<>();
    private final List<Class<?>> componentClasses = new ArrayList<>();
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final ConfigurationManager configurationManager = new ConfigurationManagerImpl(this);

    public ContextImpl(Class<?> config) {
        configurationManager.checkConfigClass(config);
        configClasses.add(config);
        initContext();
    }

    public ContextImpl(Class<?>... configs) {
        Arrays.stream(configs).forEach(configurationManager::checkConfigClass);
        configClasses.addAll(Arrays.asList(configs));
        initContext();
    }

    public ContextImpl(String packageName) {
        configClasses.addAll(configurationManager.processPackage(packageName));
        initContext();
    }

    public ContextImpl(String... packageNames) {
        Arrays.stream(packageNames)
                .forEach(packageName -> configClasses.addAll(configurationManager.processPackage(packageName)));
        initContext();
    }

    private void initContext() {
        configurationManager.processConfigClasses(configClasses);
        new BeanCreatorImpl(this, beanCreateMethods, componentClasses).createBeans();
        new BeanPostProcessorsProcessor(this, beanPostProcessors).processBeanPostProcessors();
        log.debug("Context has been initialized!");
    }

    @Override
    public <C> C getBean(Class<C> beanClass) {
        return (C) beansByType.values().stream().filter(c -> beanClass.isAssignableFrom(c.getClass())).findFirst()
                .orElseThrow(() -> new RuntimeException("No such bean!"));
    }

    @Override
    public <C> C getBean(String beanName) {
        Object bean = beansByName.get(beanName);
        if (bean == null) throw new RuntimeException("No such bean!");
        return (C) bean;
    }

    @Override
    public Map<String, Object> getAllBeans() {
        return new HashMap<>(beansByName);
    }

    @Override
    public void addBean(String beanName, Class<?> beanClass, Object bean) {
        beansByName.put(beanName, bean);
        beansByType.put(beanClass, bean);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor bpp) {
        beanPostProcessors.add(bpp);
    }

    @Override
    public void addComponentClasses(Set<Class<?>> classes) {
        componentClasses.addAll(classes);
    }

    @Override
    public void addBeanCreateMethod(Method m, Class<?> configClass) {
        beanCreateMethods.put(m.getReturnType(), new Pair<>(m, configClass));
    }
}
