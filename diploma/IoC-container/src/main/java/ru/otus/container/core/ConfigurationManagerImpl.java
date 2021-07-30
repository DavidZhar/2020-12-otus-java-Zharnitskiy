package ru.otus.container.core;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.container.annotation.*;
import ru.otus.container.transactional.HibernateTransactionalBeanPostProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ConfigurationManagerImpl implements ConfigurationManager {

    private final Context context;

    public ConfigurationManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(Configuration.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public Set<Class<?>> processPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner(), new SubTypesScanner(false));
        return reflections.getTypesAnnotatedWith(Configuration.class);
    }

    @Override
    public void processConfigClasses(List<Class<?>> configClasses) {
        configClasses.forEach(c -> {
            checkAnnotations(c);
            addBeanMethods(c);
        });
    }

    private void checkAnnotations(Class<?> configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            String[] packages = configClass.getAnnotation(ComponentScan.class).basePackages();
            scanForComponents(packages.length > 0 ? packages : new String[]{configClass.getPackageName()});
        }

        if (configClass.isAnnotationPresent(EnableHibernateTransactionManagement.class)) {
            EnableHibernateTransactionManagement transactionManagementAnnotation = configClass
                    .getAnnotation(EnableHibernateTransactionManagement.class);
            String configFileName = transactionManagementAnnotation.configFileName();
            Class<?>[] annotatedClasses = transactionManagementAnnotation.annotatedClasses();
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration()
                    .configure(configFileName);
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            MetadataSources metadataSources = new MetadataSources(registry);
            Arrays.stream(annotatedClasses).forEach(metadataSources::addAnnotatedClass);
            Metadata metadata = metadataSources.getMetadataBuilder().build();
            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

            context.addBean("sessionFactory", SessionFactory.class, sessionFactory);
            context.addBeanPostProcessor(new HibernateTransactionalBeanPostProcessor(sessionFactory));
        }
    }

    private void scanForComponents(String[] packages) {
        Arrays.stream(packages).forEach(this::scanPackageFromComponents);
    }

    private void scanPackageFromComponents(String packageName) {
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner(), new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
        context.addComponentClasses(classes);
    }

    private void addBeanMethods(Class<?> configClass) {
        Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Bean.class))
                .forEach(m -> context.addBeanCreateMethod(m, configClass));
    }
}
