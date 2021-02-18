package ru.otus.proxy;

import ru.otus.Log;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AOPDemoIoC {

    public static <T> T get(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), clazz.getInterfaces(), new InvocationHandler() {
            Object o;
            Set<String> loggedMethods = new HashSet<>();

            {
                try {
                    o = clazz.newInstance();
                    Arrays.stream(clazz.getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(Log.class))
                            .forEach(m -> loggedMethods.add(m.getName() + Arrays.toString(m.getParameterTypes())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (loggedMethods.contains(method.getName() + Arrays.toString(method.getParameterTypes())))
                    System.out.println("My PROXY LOGGING for method: " + method.getName() + Arrays.asList(method.getParameterTypes()));
                return method.invoke(o, args);
            }
        });
    }

}
