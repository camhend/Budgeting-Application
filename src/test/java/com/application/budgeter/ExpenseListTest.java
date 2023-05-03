package com.application.budgeter;

import java.util.NoSuchElementException;
import java.time.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpenseListTest {
    
    ExpenseList expenseList = new ExpenseList();

    @Test
    public void testIsEmpty() {
        ExpenseList emptyList = new ExpenseList();
        assertTrue(emptyList.isEmpty());
    }  

    @Test
    public void testSize() {
        ExpenseList emptyList = new ExpenseList();
        assertTrue(emptyList.size() == 0);
    }   

    @Test
    public void testAdd_IncrementsSize_ExpectSizeOne() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertEquals(1, expenseList.size());
    }

    @Test
    public void testContains_EmptyList_ReturnFalse() {
        Expense expense = new Expense("shoe", "clothing", LocalDate.parse("2008-10-12"), 50);
        assertFalse(expenseList.contains( expense ));
    }

    @Test
    public void testContains_AfterAddExpense_ReturnTrue() {
        Expense expense = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertTrue(expenseList.contains( expense ));
    }

    @Test
    public void testContains_ExpenseNotInList_ReturnFalse() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10); 
        
        Expense ExpenseNotInList = new Expense("shoe", "clothing", LocalDate.parse("2008-10-12"), 50); 
        assertFalse(expenseList.contains( ExpenseNotInList ));
    }

    @Test
    public void testGet_IndexZeroAfterAddtoEmptyList() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense expectedExpense = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertTrue(expectedExpense.equals(expenseList.get(0)));
    }

    @Test
    public void testGet_AfterAddtoList() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10); // index 0
        expenseList.add("shoe", "clothing", LocalDate.parse("2008-10-12"), 50); // index 1
        Expense expectedExpense = new Expense("shoe", "clothing", LocalDate.parse("2008-10-12"), 50);
        assertTrue(expectedExpense.equals(expenseList.get(1)));
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveFromEmptyList() {
        ExpenseList emptyList = new ExpenseList();
        Expense expense = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        emptyList.remove(expense);
    }   

    @Test
    public void testRemove_RemoveExpenseFromHead_ExpenseNoLongerInList() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10); // index 0
        expenseList.add("shoe", "clothing", LocalDate.parse("2002-12-14"), 50); // index 1
        expenseList.add("groceries", "food", LocalDate.parse("2010-02-28"), 50); // index 2

        Expense expenseToRemove = new Expense ("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        expenseList.remove( expenseToRemove );
        assertFalse( expenseList.contains(expenseToRemove) );
    }

    @Test
    public void testRemove_RemoveExpenseFromMiddle_ExpenseNoLongerInList() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10); // index 0
        expenseList.add("shoe", "clothing", LocalDate.parse("2002-12-14"), 50); // index 1
        expenseList.add("groceries", "food", LocalDate.parse("2010-02-28"), 50); // index 2

        Expense expenseToRemove = new Expense ("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        expenseList.remove( expenseToRemove );
        assertFalse( expenseList.contains(expenseToRemove) );
    }

    @Test
    public void testRemove_RemoveExpenseFromTail_ExpenseNoLongerInList() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10); // index 0
        expenseList.add("shoe", "clothing", LocalDate.parse("2002-12-14"), 50); // index 1
        expenseList.add("groceries", "food", LocalDate.parse("2010-02-28"), 50); // index 2

        Expense expenseToRemove = new Expense ("groceries", "food", LocalDate.parse("2010-02-28"), 50);
        expenseList.remove( expenseToRemove );
        assertFalse( expenseList.contains(expenseToRemove) );
    }

}
