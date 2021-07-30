package ru.otus.container.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableHibernateTransactionManagement{
    String configFileName() default "hibernate.cfg.xml";

    Class<?>[] annotatedClasses() default {};
}
