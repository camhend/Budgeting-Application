package com.application.budgeter;

import java.util.NoSuchElementException;
import java.time.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpenseListTest {
    
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
    public void testIsEmpty() {
        ExpenseList emptyList = new ExpenseList();
        assertTrue(emptyList.isEmpty());
    }  

    @Test(expected = NoSuchElementException.class)
    public void testRemoveFromEmptyList() {
        ExpenseList emptyList = new ExpenseList();
        emptyList.remove(expense);
    }    

}
