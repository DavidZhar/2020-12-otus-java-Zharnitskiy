package ru.otus.container.annotation;

import ru.otus.container.aop.AspectType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<?> clazz();

    AspectType type();
}

