package ru.otus.testing;

public class MyAsserts {
    public static void assertNotNull(Object object) {
        if (object == null)
            throw new MyAssertionException("Object of type " + object.getClass().getSimpleName() + " is null.");
    }

    public static void assertEquals(Object object1, Object object2) {
        if (!object1.equals(object2)) throw new MyAssertionException("Objects are not equal");
    }
}
