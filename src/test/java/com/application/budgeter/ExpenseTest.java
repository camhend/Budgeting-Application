package com.application.budgeter;

import java.time.*;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpenseTest {
    
    Expense expense;

    @Before
    public void initialize() {
        expense = new Expense(
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
    public void testGetLocalDate() {
        assertEquals(LocalDate.parse("2001-01-01"), expense.getLocalDate());
    }

    @Test 
    public void testGetCategory() {
        assertEquals("food", expense.getCategory());
    }

    @Test 
    public void testGetAmount() {
        assertEquals(15.0, expense.getAmount(), 0.001);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetAmount_NegativeNumer() {
        Expense expense = new Expense(
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            10);
        expense.setAmount(-100);
    }    

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorAmount_NegativeNumber() {
        Expense expense = new Expense(
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            -100);
    }    

    @Test
    public void testEquals_ExpensesEqual() {
        Expense expense = new Expense(
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
        
        Expense other = new Expense(
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
            assertTrue(expense.equals(other));
    }

    @Test
    public void testEquals_ExpensesNotEqual() {
        Expense expense = new Expense(
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
        
        Expense other = new Expense(
            "other", 
            "clothes", 
            LocalDate.parse("2010-12-01"), 
            30);

            assertFalse(expense.equals(other));
    }

    @Test
    public void testEquals_NotAnExpense() {
        Expense expense = new Expense(
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
        
        String test = "testObject";

        assertFalse(expense.equals(test));
    }

    @Test
    public void testToString() {
        Expense expense = new Expense(
            "testname", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
        
        String expectedString = "[testname, food, 2001-01-01, 15.0]";
        assertEquals(expectedString, expense.toString());
    }

    @Test
    public void testLexicographicNameComparator() {
        Expense a = new Expense(
            "apple", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);

            Expense b = new Expense(
                "crackers", 
                "food", 
                LocalDate.parse("2001-01-01"), 
                15);

            Expense c = new Expense(
                    "apple", 
                    "food", 
                    LocalDate.parse("2010-01-01"), 
                    35);
        
            Comparator<Expense> com = new LexicographicNameComparitor();
            assertTrue(com.compare(a, b) < 0);
            assertTrue(com.compare(b, a) > 0);
            assertTrue(com.compare(a, c) == 0);
    }

    @Test
    public void testLexicographicCategoryComparator() {
        Expense a = new Expense(
            "apple", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);

        Expense b = new Expense(
            "belt", 
            "new clothes", 
            LocalDate.parse("2001-01-01"), 
            15);

        Expense c = new Expense(
            "crackers", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);
            
        
            Comparator<Expense> com = new LexicographicCategoryComparitor();
            assertTrue(com.compare(a, b) < 0);
            assertTrue(com.compare(b, a) > 0);
            assertTrue(com.compare(a, c) == 0);
    }

    @Test
    public void testDateComparator() {
        Expense a = new Expense(
            "apple", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);

            Expense b = new Expense(
                "apple", 
                "food", 
                LocalDate.parse("2005-01-01"), 
                15);

            Expense c = new Expense(
                    "crackers", 
                    "food", 
                    LocalDate.parse("2001-01-01"), 
                    35);
        
            Comparator<Expense> com = new DateComparitor();
            assertTrue(com.compare(a, b) < 0);
            assertTrue(com.compare(b, a) > 0);
            assertTrue(com.compare(a, c) == 0);
    }

    @Test
    public void testAmountComparator() {
        Expense a = new Expense(
            "apple", 
            "food", 
            LocalDate.parse("2001-01-01"), 
            15);

            Expense b = new Expense(
                "apple", 
                "food", 
                LocalDate.parse("2005-01-01"), 
                35);

            Expense c = new Expense(
                    "crackers", 
                    "food", 
                    LocalDate.parse("2001-01-01"), 
                    15);
        
            Comparator<Expense> com = new AmountComparitor();
            assertTrue(com.compare(a, b) < 0);
            assertTrue(com.compare(b, a) > 0);
            assertTrue(com.compare(a, c) == 0);
    }


}