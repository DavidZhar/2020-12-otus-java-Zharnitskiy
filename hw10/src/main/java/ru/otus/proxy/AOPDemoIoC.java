package ru.otus.proxy;

import ru.otus.Log;

import java.lang.reflect.*;
import java.util.Arrays;

public class AOPDemoIoC {

    public static <T> T get(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), clazz.getInterfaces(), new InvocationHandler() {
            Object o;

            {
                try {
                    o = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (clazz.getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class))
                    System.out.println("My PROXY LOGGING for method: " + method.getName() + Arrays.asList(method.getParameterTypes()));
                return method.invoke(o, args);
            }
        });
    }

}
