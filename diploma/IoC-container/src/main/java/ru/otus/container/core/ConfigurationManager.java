package ru.otus.container.core;

import java.util.List;
import java.util.Set;

public interface ConfigurationManager {

    public void checkConfigClass(Class<?> configClass);

    public Set<Class<?>> processPackage(String packageName);

    void processConfigClasses(List<Class<?>> configClasses);
}
