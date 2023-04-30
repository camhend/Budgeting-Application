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
            LocalDate.parse("2001-01-01"), 
            "food", 
            15);
    }
    
    @Test
    public void getNameTest() {
        assertEquals("testname", expense.getName());
    }

    @Test
    public void getIdTest() {
        assertEquals(1, expense.getID());
    }

    @Test 
    public void getLocalDateTest() {
        assertEquals(LocalDate.parse("2001-01-01"), expense.getLocalDate());
    }

    @Test 
    public void getCategoryTest() {
        assertEquals("food", expense.getCategory());
    }

    @Test 
    public void getAmountTest() {
        assertEquals(15, expense.getAmount());
    }

    @Test
    public void equalsTest() {
        Expense other = new Expense(
            1, 
            "testname", 
            LocalDate.parse("2001-01-01"), 
            "food", 
            15);
            assertTrue(expense.equals(other));
    }
    

}
