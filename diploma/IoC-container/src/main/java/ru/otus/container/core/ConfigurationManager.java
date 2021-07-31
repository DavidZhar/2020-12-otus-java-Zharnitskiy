package ru.otus.container.core;

import java.util.List;
import java.util.Set;

public interface ConfigurationManager {

    void checkConfigClass(Class<?> configClass);

    Set<Class<?>> processPackage(String packageName);

    void processConfigClasses(List<Class<?>> configClasses);
}
