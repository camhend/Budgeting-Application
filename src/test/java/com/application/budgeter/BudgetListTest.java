package com.application.budgeter;

import java.util.ArrayList;
import java.time.*;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before;

public class BudgetListTest {
    
    BudgetList list;
    Budget budget1;
    Budget budget2;

    @Before
    public void initialize() {
        list = new BudgetList(LocalDate.now());
        budget1 = new Budget("Food", 200, 500);
        budget2 = new Budget("Rent", 1500, 1500);
    }
    

    @Test
    public void testAdd() {
        list.add(budget1);
        assertEquals(1, list.getBudgetList().size());
        assertEquals(500, list.getTotalIncome(), 0.01);
    }
    
    @Test
    public void testRemoveBudget() {
        list.add(budget1);
        list.remove(budget1);
        assertEquals(0, list.getBudgetList().size());
        assertEquals(0, list.getTotalIncome(), 0.01);
        assertEquals(0, list.getTotalSpent(), 0.01);
        assertEquals(0, list.getTotalRemaining(), 0.01);
    }

    @Test
    public void testEditBudget() {
        list.add(budget1);
        Budget budget1edit = new Budget("clothes", 200, 300);
        list.edit(budget1, budget1edit);
        assertEquals(1, list.getBudgetList().size());
        assertEquals(300, list.getTotalIncome(), 0.01);
        assertEquals(200, list.getTotalSpent(), 0.01);
        assertEquals(100, list.getTotalRemaining(), 0.01);
    }

    @Test
    public void testClearlist() {
        list.add(budget1);
        list.add(budget2);
        list.clear();
        assertEquals(0, list.getBudgetList().size());
        assertEquals(0, list.getTotalIncome(), 0.01);
        assertEquals(0, list.getTotalSpent(), 0.01);
        assertEquals(0, list.getTotalRemaining(), 0.01);
    }

    @Test
    public void testGetCategoryList() {
        list.add(budget1);
        list.add(budget2);
        ArrayList<String> categories = list.getCategoryList();
        assertTrue(categories.contains("Food"));
        assertTrue(categories.contains("Rent"));
    }
}