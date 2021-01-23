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

        Arrays.stream(classes).forEach(clazz -> { // for each test class
            Method[] methods = clazz.getDeclaredMethods();
            List<Method> testMethods = extractMethodsWithAnnotation(methods, Test.class);
            List<Method> beforeMethods = extractMethodsWithAnnotation(methods, Before.class);
            List<Method> afterMethods = extractMethodsWithAnnotation(methods, After.class);
            testAmount.addAndGet(testMethods.size());

            testMethods.forEach(method -> { // for each test method
                Object testClassInstance = null;
                try {
                    Constructor constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    testClassInstance = constructor.newInstance(); // create new instance of test class for each method
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    System.out.println("Could not create test class instance.");
                    return;
                }

                try {
                    boolean beforeMethodsDone = true;
                    try {
                        runMethods(beforeMethods, testClassInstance); // run all methods with @Before
                    } catch (Exception e) {
                        beforeMethodsDone = false;
                    }

                    if (beforeMethodsDone) { // invoke current @Test method if all @Before methods passed
                        method.invoke(testClassInstance);
                        passedAmount.addAndGet(1); // increment PASSED amount if @Test hasn't thrown an exception
                    }
                } catch (Exception e) {
                    System.out.println(e.getCause().getMessage()); // show message of any exception from current @Test
                } finally {
                    try {
                        runMethods(afterMethods, testClassInstance); // run all methods with @After
                    } catch (Exception e) {
                        System.out.println(e.getCause().getMessage());
                    }
                }
            });
        });

        System.out.println("Tests PASSED: " + passedAmount.get() + ". Tests FAILED: " + (testAmount.get() - passedAmount.get())
                + " Total: " + testAmount.get());
    }

    private static void runMethods(List<Method> beforeMethods, Object finalTestClass) {
        beforeMethods.forEach(method1 -> {
            try {
                method1.invoke(finalTestClass);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println(e.getCause().getMessage());
            }
        });
    }

    private static List<Method> extractMethodsWithAnnotation(Method[] methods, Class<? extends Annotation> clazz) {
        return Arrays.stream(methods).filter(method -> method.isAnnotationPresent(clazz)).collect(Collectors.toList());
    }
}
