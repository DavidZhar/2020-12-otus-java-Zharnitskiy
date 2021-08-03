package ru.otus.container.annotation;

import ru.otus.container.transactional.Isolation;
import ru.otus.container.transactional.Propagation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

    Propagation propagation() default Propagation.REQUIRED;

    Isolation isolation() default Isolation.READ_COMMITTED;
}
