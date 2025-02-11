package com.retailpulse.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    public void testHelloWorld() {
        String expected = "Hello, World!";
        String actual = getHelloWorld();
        assertEquals(expected, actual);
    }

    private String getHelloWorld() {
        return "Hello, World!";
    }
}
