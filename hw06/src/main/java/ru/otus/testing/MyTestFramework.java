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

        runTestClasses(testAmount, passedAmount, classes);

        showResults(testAmount, passedAmount);
    }

    private static void runTestClasses(AtomicInteger testAmount, AtomicInteger passedAmount, Class<?>[] classes) {
        Arrays.stream(classes).forEach(clazz -> {
            Method[] methods = clazz.getDeclaredMethods();
            List<Method> testMethods = extractMethodsWithAnnotation(methods, Test.class);
            List<Method> beforeMethods = extractMethodsWithAnnotation(methods, Before.class);
            List<Method> afterMethods = extractMethodsWithAnnotation(methods, After.class);
            testAmount.addAndGet(testMethods.size());

            runTestMethods(passedAmount, clazz, testMethods, beforeMethods, afterMethods);
        });
    }

    private static void runTestMethods(AtomicInteger passedAmount, Class<?> clazz, List<Method> testMethods,
                                       List<Method> beforeMethods, List<Method> afterMethods) {
        testMethods.forEach(method -> {
            Object testClassInstance = createInstance(clazz);
            if (testClassInstance == null) return;

            try {
                boolean beforeMethodsDone = runBeforeMethods(beforeMethods, testClassInstance);

                if (beforeMethodsDone) {
                    method.invoke(testClassInstance);
                    passedAmount.addAndGet(1);
                }
            } catch (Exception e) {
                System.out.println(e.getCause().getMessage());
            } finally {
                runAfterMethods(afterMethods, testClassInstance);
            }
        });
    }

    private static Object createInstance(Class<?> clazz) {
        Object testClassInstance;
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            testClassInstance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.out.println("Could not create test class instance.");
            return null;
        }
        return testClassInstance;
    }

    private static boolean runBeforeMethods(List<Method> beforeMethods, Object testClassInstance) {
        try {
            runMethods(beforeMethods, testClassInstance);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void runAfterMethods(List<Method> afterMethods, Object testClassInstance) {
        try {
            runMethods(afterMethods, testClassInstance);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
        }
    }

    private static void showResults(AtomicInteger testAmount, AtomicInteger passedAmount) {
        System.out.println("Tests PASSED: " + passedAmount.get() +
                ". Tests FAILED: " + (testAmount.get() - passedAmount.get())
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
