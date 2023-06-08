package com.application.budgeter;

import java.util.ArrayList;
import java.time.*;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before;

public class BudgetTest {
    @Test
    public void testConstructor() {
        Budget budget = new Budget("Food", 200, 500);
        assertEquals("Food", budget.getCategory());
        assertEquals(200, budget.getSpent(), 0.01);
        assertEquals(300, budget.getRemaining(), 0.01);
        assertEquals(500, budget.getTotal(), 0.01);
    }

    @Test
    public void testSetCategory() {
        Budget budget = new Budget("Food", 200, 500);
        budget.setCategory("clothes");
        assertEquals("clothes", budget.getCategory());
    }

    @Test
    public void testSetSpent() {
        Budget budget = new Budget("Food", 200, 500);
        budget.setSpent(300);
        assertEquals(300, budget.getSpent(), 0.01);
        assertEquals(200, budget.getRemaining(), 0.01);
    }

    @Test
    public void testSetTotal() {
        Budget budget = new Budget("Food", 200, 500);
        budget.setTotal(300);
        assertEquals(300, budget.getTotal(), 0.01);
        assertEquals(100, budget.getRemaining(), 0.01);
    }
}
