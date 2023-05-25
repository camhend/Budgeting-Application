package com.application.budgeter;

import java.time.*;
import java.util.*;
import java.io.*;

public class ExpenseModel {
    private Map<String, ExpenseList> loadedLists;

    public ExpenseModel() {
        this.loadedLists = new HashMap<>();
    }


    // get all csv files in Budgeter Directory with the format YYYY-MM.csv
    public ArrayList<String> getDateList() {
        ArrayList<String> dateList = new ArrayList<>();

        String projectRootPath = System.getProperty("user.dir");
        File directory = new File(projectRootPath);

        // Make sure the directory exists
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File fileValue : files) {
                // if file is a csv file and matches the pattern YYYY-MM.csv
                String file = fileValue.getName();
                boolean matchesPattern = file.matches("\\d{4}-\\d{2}\\.csv");
                if (file.endsWith(".csv") && matchesPattern ) {
                    dateList.add(file.substring(0, file.length() - 4));
                }
            }
            return dateList;
        } else {
            System.out.println("Error: Directory does not exist");
            return dateList;
        }
    }

    // Get the ExpenseList of the same Month and Year of the given LocalDate.
    // Then, put the ExpenseList in the loadedLists map for later access.
    public ExpenseList getExpenseList(LocalDate monthYear) {
        String dateKey = "";
        if (monthYear.getMonthValue() > 9) {
            dateKey = monthYear.getYear() + "-" + monthYear.getMonthValue();
        } else {
            dateKey = monthYear.getYear() + "-0" + monthYear.getMonthValue();
        }
        if (loadedLists.containsKey(dateKey)) {
            return loadedLists.get(dateKey);
        } else {
            ExpenseList newList = new ExpenseList();
            String filename = dateKey + ".csv";
            newList.loadFromCSV(filename);
            loadedLists.put(dateKey, newList);
            System.out.println("Loaded " + dateKey + ".csv");

            return newList;
        }
    }

    // Save or cancel changes on all loaded ExpenseLists. 
    // If true, save changes to the ExpenseList and save the changes to file path.
    // If false, changes are reverted on all lists.
    public void saveAll(boolean confirm) {
        String filename;
        for (String dateKey : loadedLists.keySet()) {
            ExpenseList list = loadedLists.get(dateKey);
            filename = dateKey + ".csv";
            list.confirmSave(confirm,  filename);
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
