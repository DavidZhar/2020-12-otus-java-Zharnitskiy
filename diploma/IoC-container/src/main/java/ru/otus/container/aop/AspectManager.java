package ru.otus.container.aop;

import java.util.*;

public class AspectManager {
    private static final List<CustomAspect> aspects = new ArrayList<>();
    private static final Map<Class<?>, Object> aspectsByClass = new HashMap<>();

    public static void configureAspects(Collection<Object> beans) {
        beans.forEach(b -> {
            if (CustomAspect.class.isAssignableFrom(b.getClass())) {
                aspectsByClass.put(b.getClass(), b);
            }
        });
    }

    public static CustomAspect getAspectByType(Class<?> clazz) {
        return (CustomAspect) aspectsByClass.get(clazz);
    }
}
