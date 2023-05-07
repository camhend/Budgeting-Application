package com.application.budgeter;

import org.junit.Test;
import static org.junit.Assert.*;

public class test {
    @Test
    public void TEST() {
        assertEquals(5, add(2, 3));
    }

    // create addition method
    public int add(int a, int b) {
        return a + b;
    }
}


