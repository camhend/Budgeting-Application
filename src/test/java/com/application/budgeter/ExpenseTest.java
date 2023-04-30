package com.application.budgeter;

import java.time.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpenseTest {
    
    Expense expense;

    @Before
    public void initialize() {
        expense = new Expense(
            1, 
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
    }
    
    @Test
    public void testGetName() {
        assertEquals("testname", expense.getName());
    }

    @Test
    public void testGetId() {
        assertEquals(1, expense.getID());
    }

    @Test 
    public void testGetLocalDate() {
        assertEquals(LocalDate.parse("2001-01-01"), expense.getLocalDate());
    }

    @Test 
    public void testGetCategory() {
        assertEquals("food", expense.getCategory());
    }

    @Test 
    public void testGetAmount() {
        assertEquals(15, expense.getAmount());
    }

    @Test
    public void testEquals() {
        Expense expense = new Expense(
            1, 
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
        
        Expense other = new Expense(
            1, 
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
            assertTrue(expense.equals(other));
    }


}
