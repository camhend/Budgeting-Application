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
        list.add("hotdog", "food", LocalDate.parse("2001-01-01"), 10.0);
        assertEquals(1, list.size());
        assertEquals(10, list.getTotalSpending(), 0.001);
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
        assertEquals(90, list.getTotalSpending(), 0.001);

        list.remove( exp2 ); 

        assertEquals(2, list.size());
        assertEquals(60.0, list.getTotalSpending(), 0.001);
        assertEquals(0, list.getCategorySpending("clothing"), 0.001);
        assertEquals(60, list.getCategorySpending("food"), 0.001);

    }

    @Test
    public void testRemove_TwoElementInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 30);

        list.add(exp1);
        list.add(exp2);
        list.remove(exp1);

        Expense[] expected = {exp2};
        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 0;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testRemove_OneElementInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);

        list.add(exp1);
        list.remove(exp1);

        for (Expense expense : list) {
            assertEquals(null, expense);
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        while ( itPrev.hasNext() ) {
            assertEquals(null, itPrev.next() );
        }
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
    public void testEdit_ExpenseNotInList_ReturnFalse() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        
        list.add(exp1);
        list.add(exp2);

        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);
        Expense updatedExp3 = new Expense("burger", "food", LocalDate.parse("2011-02-28"), 50);
        
        assertFalse(list.edit(exp3, updatedExp3));
    }

    @Test
    public void testEdit_EmptyList_ReturnFalse() {
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);
        Expense updatedExp3 = new Expense("burger", "food", LocalDate.parse("2011-02-28"), 50);
        
        assertFalse(list.edit(exp3, updatedExp3));
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
    public void testEdit_HeadNodeDateChangeOrderNotChanged_OrderingNotChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        Expense updatedExp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-02"), 10);
        list.edit(exp1, updatedExp1);
        
        Expense[] expected = {updatedExp1, exp2, exp3};

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
    public void testEdit_TailNodeDateChangeOrderNotChanged_OrderingNotChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2010-02-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        Expense updatedExp3 = new Expense("groceries", "food", LocalDate.parse("2009-02-28"), 50);
        list.edit(exp3, updatedExp3);
        
        Expense[] expected = {exp1, exp2, updatedExp3};

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
    public void testEdit_MiddleNodeDateChangeForwardButOrderNotChanged_OrderingNotChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2008-02-28"), 30);
        Expense exp4 = new Expense("video game", "entertainment", LocalDate.parse("2012-04-21"), 40);
        Expense exp5 = new Expense("gas", "gas", LocalDate.parse("2013-06-02"), 65);


        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.add(exp4);
        list.add(exp5);

        Expense updatedExp3 = new Expense("groceries", "food", LocalDate.parse("2008-03-28"), 30);
        list.edit(exp3, updatedExp3);
        
        Expense[] expected = {exp1, exp2, updatedExp3, exp4, exp5};

        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 4;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testEdit_MiddleNodeChangedPreviousButOrderNotChanged_OrderingNotChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2008-02-28"), 30);
        Expense exp4 = new Expense("video game", "entertainment", LocalDate.parse("2012-04-21"), 40);
        Expense exp5 = new Expense("gas", "gas", LocalDate.parse("2013-06-02"), 65);


        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.add(exp4);
        list.add(exp5);

        Expense updatedExp3 = new Expense("groceries", "food", LocalDate.parse("2008-01-28"), 30);
        list.edit(exp3, updatedExp3);
        
        Expense[] expected = {exp1, exp2, updatedExp3, exp4, exp5};

        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 4;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testEdit_MiddleNodeChangedPreviousDate_OrderingChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2008-02-28"), 30);
        Expense exp4 = new Expense("video game", "entertainment", LocalDate.parse("2012-04-21"), 40);
        Expense exp5 = new Expense("gas", "gas", LocalDate.parse("2013-06-02"), 65);


        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.add(exp4);
        list.add(exp5);

        Expense updatedExp3 = new Expense("groceries", "food", LocalDate.parse("2001-02-28"), 30);
        list.edit(exp3, updatedExp3);
        
        Expense[] expected = {exp1, updatedExp3, exp2, exp4, exp5};

        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 4;
        while ( itPrev.hasNext() ) {
            assertEquals(expected[index], itPrev.next() );
            index--;
        }
    }

    @Test
    public void testEdit_MiddleNodeChangedForwardDate_OrderingChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-01-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2002-12-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2008-02-28"), 30);
        Expense exp4 = new Expense("video game", "entertainment", LocalDate.parse("2012-04-21"), 40);
        Expense exp5 = new Expense("gas", "gas", LocalDate.parse("2013-06-02"), 65);


        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.add(exp4);
        list.add(exp5);

        Expense updatedExp3 = new Expense("groceries", "food", LocalDate.parse("2012-08-28"), 30);
        list.edit(exp3, updatedExp3);
        
        Expense[] expected = {exp1, exp2, exp4, updatedExp3, exp5};

        int index = 0;
        for (Expense expense : list) {
            assertEquals(expected[index], expense);
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 4;
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

    @Test
    public void testToArray_emptyList() {
        Expense[] actual = list.toArray();
        Expense[] expected = {};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToArray() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        Expense[] actual = list.toArray();
        Expense[] expected = {exp1, exp2, exp3};
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetCategorySpending_CategoryNotInList() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        list.add(exp1);

        assertEquals(-1.0, list.getCategorySpending("clothing"), 0.001);
    }

    @Test
    public void testGetCategorySpending_OneCategory() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        list.add(exp1);

        assertEquals(10, list.getCategorySpending("food"), 0.001);
    }

    @Test
    public void testGetCategorySpending_AddToExistingCategory() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("groceries", "food", LocalDate.parse("2001-02-01"), 90);
        list.add(exp1);
        list.add(exp2);

        assertEquals(100.0, list.getCategorySpending("food"), 0.001);
    }

    @Test
    public void testGetCategorySpending_RemoveFromCategory() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("groceries", "food", LocalDate.parse("2001-02-01"), 90);
        list.add(exp1);
        list.add(exp2);
        list.remove(exp2);

        assertEquals(10.0, list.getCategorySpending("food"), 0.001);
    }

    @Test
    public void testGetCategorySpending_CategoryNameNotExactMatch() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        list.add(exp1);

        assertEquals(-1, list.getCategorySpending("Food"), 0.001);
    }

    @Test
    public void testGetCategorySpending_AfterEditAmountNOTChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("groceries", "food", LocalDate.parse("2001-02-01"), 90);
        Expense updatedExp2 = new Expense("restaurant", "food", LocalDate.parse("2002-02-01"), 90);

        list.add(exp1);
        list.add(exp2);
        list.edit(exp2, updatedExp2);

        assertEquals(100, list.getCategorySpending("food"), 0.001);
    }


    @Test
    public void testGetCategorySpending_AfterEditAmountWASChanged() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("groceries", "food", LocalDate.parse("2001-02-01"), 90);
        Expense updatedExp2 = new Expense("groceries", "food", LocalDate.parse("2001-02-01"), 50);

        list.add(exp1);
        list.add(exp2);
        list.edit(exp2, updatedExp2);

        assertEquals(60, list.getCategorySpending("food"), 0.001);
    }

    @Test
    public void testGetCategorySpending_AfterCategoryNameChangeToNewCategory() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("groceries", "food", LocalDate.parse("2001-02-01"), 90);
        Expense updatedExp2 = new Expense("groceries", "essentials", LocalDate.parse("2001-02-01"), 90);

        list.add(exp1);
        list.add(exp2);
        list.edit(exp2, updatedExp2);

        assertEquals(10, list.getCategorySpending("food"), 0.001);
        assertEquals(90, list.getCategorySpending("essentials"), 0.001);
    }

    @Test
    public void testGetCategorySpending_AfterCategoryNameChangeToExistingCategory() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        Expense updatedExp2 = new Expense("restuarant", "food", LocalDate.parse("2001-04-14"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.edit(exp2, updatedExp2);

        assertEquals(110, list.getCategorySpending("food"), 0.001);
        assertEquals(0, list.getCategorySpending("clothing"), 0.001);
    }

    @Test
    public void testGetCategorySpending_AfterClear() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        list.add(exp1);

        assertEquals(10, list.getCategorySpending("food"), 0.001);
    }

    @Test
    public void testClear() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        list.clear();
        
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertEquals(0, list.getTotalSpending(), 0.001);
        assertEquals(-1.0, list.getCategorySpending("food"), 0.001);
        assertEquals(-1.0, list.getCategorySpending("clothing"), 0.001);


    }

    @Test
    public void testEquals_ExpectTrue() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        ExpenseList other = new ExpenseList();

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        
        other.add(exp1);
        other.add(exp2);
        other.add(exp3);
        
        assertTrue(list.equals(other));
    }

    @Test
    public void testEqualsRemoveThenRestore() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        ExpenseList other = new ExpenseList();

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        
        other.add(exp1);
        other.add(exp2);
        other.add(exp3);

        other.remove(exp1);
        assertFalse(list.equals(other));

        other.add(exp1);
        assertTrue(list.equals(other));

        other.remove(exp1);
        assertFalse(list.equals(other));

        other.add("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        assertTrue(list.equals(other));
        
    }
    
    @Test
    public void testEqualsAfterEdit_ExpectFalse() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        ExpenseList other = new ExpenseList();

        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        
        other.add(exp1);
        other.add(exp2);
        other.add(exp3);

        Expense updatedExp2 = new Expense("restuarant", "food", LocalDate.parse("2001-04-14"), 50);
        other.edit(exp2, updatedExp2);

        assertFalse(list.equals(other));
    }

    @Test
    public void testCopy_EmptyList() {
        ExpenseList other = list.copy();
        assertTrue(list.equals(other));

        Iterator<Expense> copyItr = other.descendingIterator();
        Iterator<Expense> listItr = list.descendingIterator();
        while (listItr.hasNext()) {
            assertTrue(listItr.next().equals(copyItr.next()));
        }

    }

    @Test
    public void testCopy_OneNode() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        list.add(exp1);

        ExpenseList other = list.copy();
        assertTrue(list.equals(other));

        Iterator<Expense> copyItr = other.descendingIterator();
        Iterator<Expense> listItr = list.descendingIterator();
        while (listItr.hasNext()) {
            assertTrue(listItr.next().equals(copyItr.next()));
        }
    }

    @Test
    public void testCopy_MultipleNodes() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        ExpenseList other = list.copy();
        assertTrue(list.equals(other));

        Iterator<Expense> copyItr = other.descendingIterator();
        Iterator<Expense> listItr = list.descendingIterator();
        while (listItr.hasNext()) {
            assertTrue(listItr.next().equals(copyItr.next()));
        }
    }

    @Test
    public void testCopy_EqualsAfterAdd_ExpectFalse() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        ExpenseList other = list.copy();

        list.add("hotdog", "food", LocalDate.parse("2001-02-01"), 10);

        assertFalse(list.equals(other));

        Iterator<Expense> copyItr = other.descendingIterator();
        Iterator<Expense> listItr = list.descendingIterator();
        
        boolean checkBackwards = true;
        while (listItr.hasNext()) {
            if ( !listItr.next().equals(copyItr.next()) ) {
                checkBackwards = false;
            }
            if ( !copyItr.hasNext() ) {
                checkBackwards = false;
                break;
            }
        }
        assertFalse(checkBackwards);
    }

    @Test
    public void testCopy_EqualsAfterAddSameExpenseDifferentMethods_ExpectTrue() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        ExpenseList other = list.copy();

        list.add("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        other.add(exp1);

        assertTrue(list.equals(other));
    }

    @Test
    public void testCopy_EqualsAfterEdit_ExpectFalse() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        ExpenseList other = list.copy();

        list.edit(exp1, new Expense("restaurant", "food", LocalDate.parse("2001-02-01"), 10));

        assertFalse(list.equals(other));

        Iterator<Expense> copyItr = other.descendingIterator();
        Iterator<Expense> listItr = list.descendingIterator();
        boolean checkBackwards = true;
        while (listItr.hasNext()) {
            if ( !listItr.next().equals(copyItr.next()) ) {
                checkBackwards = false;
            }
        }
        assertFalse(checkBackwards);
    }

    @Test
    public void testCopy_EqualsAfterRemove_ExpectFalse() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        ExpenseList other = list.copy();

        list.remove(exp2);

        assertFalse(list.equals(other));

        Iterator<Expense> copyItr = other.descendingIterator();
        Iterator<Expense> listItr = list.descendingIterator();
        boolean checkBackwards = true;
        while (listItr.hasNext()) {
            if ( !listItr.next().equals(copyItr.next()) ) {
                checkBackwards = false;
            }
        }
        assertFalse(checkBackwards);
    }

    @Test
    public void testCopy_EqualsAfterExpenseSetter_ExpectFalse() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        
        list.add(exp1);

        ExpenseList other = list.copy();

        for (Expense expense : list) {
            expense.setName("name");
        }

        assertFalse(list.equals(other));

        Iterator<Expense> copyItr = other.descendingIterator();
        Iterator<Expense> listItr = list.descendingIterator();
        boolean checkBackwards = true;
        while (listItr.hasNext()) {
            if ( !listItr.next().equals(copyItr.next()) ) {
                checkBackwards = false;
            }
        }
        assertFalse(checkBackwards);
    }

    @Test
    public void testSaveChanges_ConfirmChanges() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        list.add(exp1);
        list.add(exp2);
        list.add(exp3);

        list.saveChanges(false);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testSaveChanges_RevertChanges() {
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 50);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        
        list.add(exp1);
        list.add(exp2);
        list.add(exp3);
        list.saveChanges(true);
        
        list.remove(exp1);
        list.saveChanges(false);

        Expense[] expected = {exp1, exp2, exp3};
        int index = 0;
        for (Expense expense : list) {
            assertTrue(expected[index].equals(expense));
            index++;
        }

        Iterator<Expense> itPrev = list.descendingIterator();
        index = 2;
        while ( itPrev.hasNext() ) {
            assertTrue(expected[index].equals(itPrev.next()));
            index--;
        }
    }


}