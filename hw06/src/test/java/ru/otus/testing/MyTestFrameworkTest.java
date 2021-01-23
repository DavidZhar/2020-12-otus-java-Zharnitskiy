package ru.otus.testing;

import ru.otus.testing.annotation.After;
import ru.otus.testing.annotation.Before;
import ru.otus.testing.annotation.Test;

import static ru.otus.testing.MyAsserts.assertEquals;
import static ru.otus.testing.MyAsserts.assertNotNull;

class MyTestFrameworkTest {

    private String value;

    @Before
    void setUp() {
        value = "Value set in @Before method";
        System.out.println(value);
    }

    @Test
    void testShouldRunTestMethodAfterBeforeMethodAndNotFail() {
        assertNotNull(value);
        assertEquals("Value set in @Before method", value);
        System.out.println("Test 1: " + value);
    }

    @Test
    void testShouldFailOnAssertion() {
        assertEquals("Wrong value", value);
    }

    @After
    void tearDown() {
        value = "Value set in @After method";
        System.out.println(value);
    }
}