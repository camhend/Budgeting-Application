package com.application.budgeter;

import java.time.*;
import java.util.*;

public class ExpenseModel {
    private Map<String, ExpenseList> loadedLists;

    public ExpenseModel() {
        this.loadedLists = new HashMap<>();
    }

    // Get the ExpenseList of the same Month and Year of the given LocalDate.
    // Then, put the ExpenseList in the loadedLists map for later access.
    public ExpenseList getExpenseList(LocalDate monthYear) {
        String dateKey = monthYear.getYear() + "-" + monthYear.getMonthValue();
        if (loadedLists.containsKey(dateKey)) {
            return loadedLists.get(dateKey);
        } else {
            ExpenseList newList = new ExpenseList();
            String path = "Budgeter/src/main/resources/com/application/budgeter/expensedata/";
            String filename = dateKey + ".csv";
            newList.loadFromCSV(path + filename);
            loadedLists.put(dateKey, newList);
            return newList;
        }
    }

    // Save or cancel changes on all loaded ExpenseLists. 
    // If true, save changes to the ExpenseList and save the changes to file path.
    // If false, changes are reverted on all lists.
    public void saveAll(boolean confirm) {
        String path = "Budgeter/src/main/resources/com/application/budgeter/expensedata/";
        String filename;
        for (String dateKey : loadedLists.keySet()) {
            ExpenseList list = loadedLists.get(dateKey);
            filename = dateKey + ".csv";
            list.confirmSave(confirm, path + filename);
        }
    }

    public void add( Expense newExpense ) {
        ExpenseList list = getExpenseList(newExpense.getLocalDate());
        list.add(newExpense);
    }

    public void add ( String name, String category, LocalDate localDate, double amount) { 
        Expense newExpense = new Expense(name, category, localDate, amount);
        ExpenseList list = getExpenseList(newExpense.getLocalDate());
        list.add(newExpense);
    }

    // bugged for edit if date same
    public boolean edit( Expense old, Expense updated) {
        boolean foundOldExpense;
        if (old.getLocalDate().getMonthValue() == updated.getLocalDate().getMonthValue() &&
            old.getLocalDate().getYear() == updated.getLocalDate().getYear()) 
        {
            foundOldExpense = getExpenseList(updated.getLocalDate()).edit(old, updated);
        } else {
            foundOldExpense = getExpenseList(old.getLocalDate()).remove(old);
            if (foundOldExpense) {
                getExpenseList(updated.getLocalDate()).add(updated);
            }
        }
        return foundOldExpense;
    }

    public boolean remove( Expense expense ) {
        return getExpenseList(expense.getLocalDate()).remove(expense);
    }

}
