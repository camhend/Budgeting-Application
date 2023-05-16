package com.application.budgeter;

import java.util.Iterator;
import java.time.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpenseControllerTest {
    @Test
    public void isValidDateTest() {
        ExpenseController expenseController = new ExpenseController();
        // testing boundaries
        assertTrue(expenseController.isValidDate("01/01/9999"));
        assertTrue(expenseController.isValidDate("12/31/9999"));
        assertTrue(expenseController.isValidDate("01/01/1000"));
        assertTrue(expenseController.isValidDate("12/31/1000"));
        // month ends
        assertFalse(expenseController.isValidDate("13/31/1000"));
        assertFalse(expenseController.isValidDate("12/32/1000"));
        assertFalse(expenseController.isValidDate("02/30/1000"));
        assertTrue(expenseController.isValidDate("02/29/2020"));
        assertFalse(expenseController.isValidDate("04/31/1000"));


        // testing digits
        assertFalse(expenseController.isValidDate("01/1/1000"));
        assertFalse(expenseController.isValidDate("1/01/1000"));
        assertFalse(expenseController.isValidDate("1/1/1000"));
        
        // testing years
        assertFalse(expenseController.isValidDate("12/12/10000"));
        assertFalse(expenseController.isValidDate("12/12/999"));

        // testing 0s
        assertFalse(expenseController.isValidDate("00/00/0000"));
        assertFalse(expenseController.isValidDate("00/00/1000"));
        assertFalse(expenseController.isValidDate("00/12/1000"));
        assertFalse(expenseController.isValidDate("12/00/1000"));

        // testing negatives
        assertFalse(expenseController.isValidDate("-1/12/1000"));
        assertFalse(expenseController.isValidDate("12/-1/1000"));
        assertFalse(expenseController.isValidDate("12/12/-1000"));
    }

    @Test
    public void isValidCostTest() {
        ExpenseController expenseController = new ExpenseController();
        // testing boundaries
        assertTrue(expenseController.isValidCost("0"));
    }
    
}
