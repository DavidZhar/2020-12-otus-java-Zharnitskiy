package ru.otus.testing;

public class MyAsserts {
    public static void assertNotNull(Object object) {
        if (object == null)
            throw new AssertionError("Assertion error: Object is null.");
    }

    public static void assertEquals(Object object1, Object object2) {
        if (!object1.equals(object2)) throw new AssertionError("Assertion error: Objects are not equal");
    }
}
