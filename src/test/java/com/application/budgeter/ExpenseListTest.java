package com.application.budgeter;

import java.util.Iterator;
import java.time.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpenseListTest {
    
    ExpenseList list = new ExpenseList();

    @Test
    public void testIsEmpty() {
        assertTrue(list.isEmpty());
    }  

    @Test
    public void testSize() {
        assertTrue(list.size() == 0);
    }   

    @Test
    public void testContains_EmptyList_ReturnFalse() {
        Expense expense = new Expense("shoe", "clothing", LocalDate.parse("2008-10-12"), 50);
        assertFalse(list.contains( expense ));
    }

    @Test
    public void testContains_AfterAddExpense_ReturnTrue() {
        Expense expense = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        list.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertTrue(list.contains( expense ));
    }

    @Test
    public void testContains_ExpenseNotInList_ReturnFalse() {
        list.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10); 
        
        Expense ExpenseNotInList = new Expense("shoe", "clothing", LocalDate.parse("2008-10-12"), 50); 
        assertFalse(list.contains( ExpenseNotInList ));
    }

    @Test
    public void testExpenseListIterator() {  
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        Expense[] arr = {exp1, exp2, exp3}; // array representation of expected list
        
        int index = 0;
        for (Expense expense : list) {
            assertTrue(expense.equals(arr[index]));
            index++;
        }
    }


    @Test
    public void testDescendingExpenseListIterator() {  
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        Expense[] arr = {exp1, exp2, exp3}; // array representation of expected list
        
        Iterator<Expense> it = list.descendingIterator();
        int index = 2;
        while( it.hasNext() ) {
            assertTrue(it.next().equals(arr[index]));
            index--;
        }
    }

    @Test
    public void testForEach_EmptyList() {  
        assertFalse(list.iterator().hasNext());
    }

    
    @Test
    public void testAdd_IncrementsCounters() {
        list.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertEquals(1, list.size());
        assertEquals(10, list.getTotalSpending());
    }

    @Test
    public void testAdd_AddtoEmptyList_newExpenseIsAtIndexZero() {
        list.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense expectedExpense = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertTrue(expectedExpense.equals(list.get(0)));
    }

    @Test
    public void testAdd_AfterAddtoList_newExpenseIsAtEnd() {
        list.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10); // index 0
        list.add("shoe", "clothing", LocalDate.parse("2008-10-12"), 50); // index 1
        Expense expectedExpense = new Expense("shoe", "clothing", LocalDate.parse("2008-10-12"), 50);
        assertTrue(expectedExpense.equals(list.get(1)));
    }

    @Test
    public void testAdd_AddOlderDate_AddSortedtoEnd() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense olderExpense = new Expense("groceries", "food", LocalDate.parse("1995-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(olderExpense);
        Expense[] expected = {olderExpense, exp1, exp2};

        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 2;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }
    
    @Test
    public void testAdd_AddMiddleDate_AddSortedtoMiddle() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2020-12-14"), 50);
        Expense middleExpense = new Expense("groceries", "food", LocalDate.parse("2006-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(middleExpense);
        Expense[] expected = {exp1, middleExpense, exp2};

        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 2;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testAdd_AddNewerDate_NewerDateIsLastIndex() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2010-06-14"), 50);
        Expense newerExpense = new Expense("groceries", "food", LocalDate.parse("2020-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(newerExpense);
        Expense[] expected = {exp1, exp2, newerExpense};

        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 2;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testRemove_FromEmptyList_ExpectFalse() {
        ExpenseList emptyList = new ExpenseList();
        Expense expense = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        assertFalse( emptyList.remove(expense) );
    }   

    @Test
    public void testRemove_RemoveDecrementsCounters() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 30);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        assertEquals(3, list.size());
        assertEquals(90, list.getTotalSpending());

        list.remove( exp2 ); 

        assertEquals(2, list.size());
        assertEquals(60, list.getTotalSpending());
    }

    @Test
    public void testRemove_RemoveExpenseFromHead_ExpenseNoLongerInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.remove(exp1);
        
        Expense[] expected = {exp2, exp3};
        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 1;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testRemove_RemoveExpenseFromMiddle_ExpenseNoLongerInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.remove(exp2);

        Expense[] expected = {exp1, exp3};
        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 1;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testRemove_RemoveExpenseFromTail_ExpenseNoLongerInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.remove(exp3);

        Expense[] expected = {exp1, exp2};
        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 1;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testEdit_DateNotChanged_OrderingNotChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        Expense updatedExp2 = new Expense("shoe", "shoes", LocalDate.parse("2002-12-14"), 50);
        list.edit(exp2, updatedExp2);
        
        Expense[] expected = {exp1, updatedExp2, exp3};

        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 2;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testEdit_HeadNodeDateChange() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        Expense updatedExp1 = new Expense("hamburger", "food", LocalDate.parse("2001-05-01"), 10);
        list.edit(exp1, updatedExp1);
        
        Expense[] expected = {exp2, updatedExp1, exp3};
        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 2;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testEdit_TailNodeDateChange() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        Expense updatedExp3 = new Expense("groceries", "food", LocalDate.parse("2001-01-28"), 50);
        list.edit(exp3, updatedExp3);

        Expense[] expected = {updatedExp3, exp1, exp2};
        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 2;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }


}
