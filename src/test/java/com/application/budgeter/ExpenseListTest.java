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
    public void testAdd_IncrementsCounters() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertEquals(1, expenseList.size());
        assertEquals(10, expenseList.getTotalSpending());
    }


    @Test
    public void testAdd_AddtoEmptyList_newExpenseIsAtIndexZero() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense expectedExpense = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertTrue(expectedExpense.equals(expenseList.get(0)));
    }

    @Test
    public void testAdd_AfterAddtoList_newExpenseIsAtEnd() {
        expenseList.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10); // index 0
        expenseList.add("shoe", "clothing", LocalDate.parse("2008-10-12"), 50); // index 1
        Expense expectedExpense = new Expense("shoe", "clothing", LocalDate.parse("2008-10-12"), 50);
        assertTrue(expectedExpense.equals(expenseList.get(1)));
    }

    @Test
    public void testAdd_AddOlderDate_OlderDateIsLowerIndex() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense olderExpense = new Expense("groceries", "food", LocalDate.parse("1995-02-28"), 50);

        expenseList.add(exp1);
        expenseList.add(exp2);
        expenseList.add(olderExpense);

        assertTrue(expenseList.get(0).equals(olderExpense));
        assertTrue(expenseList.get(1).equals(exp1));
        assertTrue(expenseList.get(2).equals(exp2));
    }
    
    @Test
    public void testAdd_AddMiddleDate_MiddleDateIsMiddleIndex() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2020-12-14"), 50);
        Expense middleExpense = new Expense("groceries", "food", LocalDate.parse("2006-02-28"), 50);

        expenseList.add(exp1);
        expenseList.add(exp2);
        expenseList.add(middleExpense);

        assertTrue(expenseList.get(0).equals(exp1));
        assertTrue(expenseList.get(1).equals(middleExpense));
        assertTrue(expenseList.get(2).equals(exp2));
    }

    @Test
    public void testAdd_AddNewerDate_NewerDateIsLastIndex() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2010-06-14"), 50);
        Expense newerExpense = new Expense("groceries", "food", LocalDate.parse("2020-02-28"), 50);

        expenseList.add(exp1);
        expenseList.add(exp2);
        expenseList.add(newerExpense);

        assertTrue(expenseList.get(0).equals(exp1));
        assertTrue(expenseList.get(1).equals(exp2));
        assertTrue(expenseList.get(2).equals(newerExpense));
    }

    @Test
    public void testRemoveFromEmptyList_ExpectFalse() {
        ExpenseList emptyList = new ExpenseList();
        Expense expense = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertFalse( emptyList.remove(expense) );
    }   

    @Test
    public void testRemove_RemoveExpenseFromHead_ExpenseNoLongerInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        expenseList.add(exp1);
        expenseList.add(exp2);
        expenseList.add(exp3);

        assertTrue( expenseList.contains(exp1) ); // exp1 in list
        assertTrue(expenseList.remove( exp1 ) ); // item successfully removed; return true
        assertFalse( expenseList.contains(exp1) ); // exp1 removed

        assertEquals(exp2, expenseList.get(0)); // exp2 is the new head
    }

    @Test
    public void testRemove_RemoveExpenseFromMiddle_ExpenseNoLongerInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        expenseList.add(exp1);
        expenseList.add(exp2);
        expenseList.add(exp3);

        assertTrue( expenseList.contains(exp2) ); // exp2 in list
        assertTrue(expenseList.remove( exp2 ) ); // item successfully removed; return true
        assertFalse( expenseList.contains(exp2) ); // exp2 removed
    }

    @Test
    public void testRemove_RemoveExpenseFromTail_ExpenseNoLongerInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        expenseList.add(exp1);
        expenseList.add(exp2);
        expenseList.add(exp3);

        assertTrue( expenseList.contains(exp3) ); // exp3 in list
        assertTrue(expenseList.remove( exp3 ) ); // item successfully removed; return true
        assertFalse( expenseList.contains(exp3) ); // exp3 removed
    }

    @Test
    public void testForEach() {  
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        expenseList.add(exp1);
        expenseList.add(exp2);
        expenseList.add(exp3);
        Expense[] arr = {exp1, exp2, exp3}; // array representation of expected list
        
        int index = 2; // ExpenseList.iterator() starts at tail
        for (Expense expense : expenseList) {
            assertTrue(expense.equals(arr[index]));
            index--;
        }
    }

    @Test
    public void testForEach_EmptyList() {  
        assertFalse(expenseList.iterator().hasNext());
    }

}
