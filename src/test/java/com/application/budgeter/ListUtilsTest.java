package com.application.budgeter;

import java.util.*;
import java.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListUtilsTest {
    
    @Test
    public void testSortName() {
        ArrayList<Expense> list = new ArrayList<>();
        
        Expense exp1 = new Expense("apple", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("banana", "clothing", LocalDate.parse("2001-04-14"), 20);
        Expense exp3 = new Expense("carrot", "food", LocalDate.parse("2001-06-28"), 30);
        Expense exp4 = new Expense("dressshoes", "clothing", LocalDate.parse("2001-04-14"), 40);
        Expense exp5 = new Expense("eggplant", "food", LocalDate.parse("2001-06-28"), 50);
        Expense exp6 = new Expense("fish", "clothing", LocalDate.parse("2001-04-14"), 60);
        Expense exp7 = new Expense("gas", "food", LocalDate.parse("2001-06-28"), 70);
        Expense exp8 = new Expense("hat", "clothing", LocalDate.parse("2001-04-14"), 80);
        Expense exp9 = new Expense("icecrean", "food", LocalDate.parse("2001-06-28"), 90);
        Expense exp10 = new Expense("jeans", "clothing", LocalDate.parse("2001-04-14"), 100);
        
        list.add(exp5);
        list.add(exp1);
        list.add(exp8);
        list.add(exp4);
        list.add(exp6);
        list.add(exp7);
        list.add(exp2);
        list.add(exp10);
        list.add(exp3);
        list.add(exp9);

        System.out.println("Before:");
        for (Expense e : list) {
            System.out.println(e);
        }

        System.out.println();
        System.out.println("After:");
        ListUtils.sort(list, new AmountComparitor());
        for (Expense e : list) {
            System.out.println(e);
        }

    }

    @Test
    public void testSortCaregory() {
        ArrayList<Expense> list = new ArrayList<>();
        
        Expense exp1 = new Expense("apple", "aaa", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("apple", "bbb", LocalDate.parse("2001-02-01"), 10);
        Expense exp3 = new Expense("apple", "cc", LocalDate.parse("2001-02-01"), 10);
        Expense exp4 = new Expense("apple", "ddd", LocalDate.parse("2001-02-01"), 10);
        Expense exp5 = new Expense("apple", "eee", LocalDate.parse("2001-02-01"), 10);
        Expense exp6 = new Expense("apple", "fff", LocalDate.parse("2001-02-01"), 10);
        Expense exp7 = new Expense("apple", "gggg", LocalDate.parse("2001-02-01"), 10);
        Expense exp8 = new Expense("apple", "hhh", LocalDate.parse("2001-02-01"), 10);
        Expense exp9 = new Expense("apple", "iii", LocalDate.parse("2001-02-01"), 10);
        Expense exp10 = new Expense("apple", "jjj", LocalDate.parse("2001-02-01"), 10);
        
        list.add(exp5);
        list.add(exp1);
        list.add(exp8);
        list.add(exp4);
        list.add(exp6);
        list.add(exp7);
        list.add(exp2);
        list.add(exp10);
        list.add(exp3);
        list.add(exp9);

        System.out.println("Before:");
        for (Expense e : list) {
            System.out.println(e);
        }

        System.out.println();
        System.out.println("After:");
        ListUtils.sort(list, new LexicographicCategoryComparitor());
        for (Expense e : list) {
            System.out.println(e);
        }

    }

    @Test
    public void testSortDate() {
        ArrayList<Expense> list = new ArrayList<>();
        
        Expense exp1 = new Expense("apple", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("apple", "clothing", LocalDate.parse("2002-04-14"), 10);
        Expense exp3 = new Expense("apple", "food", LocalDate.parse("2003-06-28"), 10);
        Expense exp4 = new Expense("apple", "clothing", LocalDate.parse("2004-04-14"), 10);
        Expense exp5 = new Expense("apple", "food", LocalDate.parse("2005-06-28"), 10);
        Expense exp6 = new Expense("apple", "clothing", LocalDate.parse("2006-04-14"), 10);
        Expense exp7 = new Expense("apple", "food", LocalDate.parse("2007-06-28"), 10);
        Expense exp8 = new Expense("apple", "clothing", LocalDate.parse("2008-04-14"), 10);
        Expense exp9 = new Expense("apple", "food", LocalDate.parse("2009-06-28"), 10);
        Expense exp10 = new Expense("apple", "clothing", LocalDate.parse("2010-04-14"), 10);
        
        list.add(exp5);
        list.add(exp1);
        list.add(exp8);
        list.add(exp4);
        list.add(exp6);
        list.add(exp7);
        list.add(exp2);
        list.add(exp10);
        list.add(exp3);
        list.add(exp9);

        System.out.println("Before:");
        for (Expense e : list) {
            System.out.println(e);
        }

        System.out.println();
        System.out.println("After:");
        ListUtils.sort(list, new DateComparitor());
        for (Expense e : list) {
            System.out.println(e);
        }

    }

    @Test
    public void testSortAmount() {
        ArrayList<Expense> list = new ArrayList<>();
        
        Expense exp1 = new Expense("hotdog", "food", LocalDate.parse("2001-02-01"), 10);
        Expense exp2 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 20);
        Expense exp3 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 30);
        Expense exp4 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 40);
        Expense exp5 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 50);
        Expense exp6 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 60);
        Expense exp7 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 70);
        Expense exp8 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 80);
        Expense exp9 = new Expense("groceries", "food", LocalDate.parse("2001-06-28"), 90);
        Expense exp10 = new Expense("shoe", "clothing", LocalDate.parse("2001-04-14"), 100);
        
        list.add(exp5);
        list.add(exp1);
        list.add(exp8);
        list.add(exp4);
        list.add(exp6);
        list.add(exp7);
        list.add(exp2);
        list.add(exp10);
        list.add(exp3);
        list.add(exp9);

        System.out.println("Before:");
        for (Expense e : list) {
            System.out.println(e);
        }

        System.out.println();
        System.out.println("After:");
        ListUtils.sort(list, new AmountComparitor());
        for (Expense e : list) {
            System.out.println(e);
        }

    }

    


}
