package ru.otus.testing;

import ru.otus.testing.annotation.After;
import ru.otus.testing.annotation.Before;
import ru.otus.testing.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MyTestFramework {

    public static void runTests(Class<?>... classes) {
        AtomicInteger testAmount = new AtomicInteger();
        AtomicInteger passedAmount = new AtomicInteger();

        Arrays.stream(classes).forEach(clazz -> {
            Method[] methods = clazz.getDeclaredMethods();
            List<Method> testMethods = checkAnnotation(methods, Test.class);
            List<Method> beforeMethods = checkAnnotation(methods, Before.class);
            List<Method> afterMethods = checkAnnotation(methods, After.class);
            testAmount.addAndGet(testMethods.size());

            testMethods.forEach(method -> {
                Object testClassInstance = null;
                try {
                    Constructor constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    testClassInstance = constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    System.out.println("Could not create test class instance.");
                    return;
                }

                try {
                    boolean beforeMethodsDone = true;
                    try {
                        runMethods(beforeMethods, testClassInstance);
                    } catch (Exception e) {
                        beforeMethodsDone = false;
                    }

                    if (beforeMethodsDone) {
                        method.invoke(testClassInstance);
                        passedAmount.addAndGet(1);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.out.println(e.getCause().getMessage());
                } finally {
                    try {
                        runMethods(afterMethods, testClassInstance);
                    } catch (Exception e) {
                        System.out.println(e.getCause().getMessage());
                    }
                }
            });
        });

        System.out.println("Tests PASSED: " + passedAmount.get() + ". Tests FAILED: " + (testAmount.get() - passedAmount.get()));
    }

    private static void runMethods(List<Method> beforeMethods, Object finalTestClass) {
        beforeMethods.forEach(method1 -> {
            try {
                method1.invoke(finalTestClass);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private static List<Method> checkAnnotation(Method[] methods, Class<? extends Annotation> clazz) {
        return Arrays.stream(methods).filter(method -> method.isAnnotationPresent(clazz)).collect(Collectors.toList());
    }
}
