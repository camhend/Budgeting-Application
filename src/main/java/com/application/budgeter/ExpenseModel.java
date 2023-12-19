package com.application.budgeter;

import java.time.*;
import java.util.*;
import java.io.*;
/*  Authors: Sukhnain Deol, Cameron Henderson, Theodore Ingberman, and Kristopher McFarland
 *  Date: 06/2023
 *  Description: Manages ExpenseLists for all months. Loads lists from CSV files as needed, 
 *      and saves changes to CSV storage. Add, edit, and remove methods ensure operations
 *      are performed on the correct ExpenseList of the correct month.
 */
public class ExpenseModel {
    private Map<String, ExpenseList> loadedLists;
    private String projectRootPath;
    private File directory;
    private ArrayList<String> dateList;

    public ExpenseModel() {
        this.loadedLists = new HashMap<>();
        this.projectRootPath = System.getProperty("user.dir") + "\\expensedata";
        this.directory = new File(projectRootPath);
        initializeDateList();
    }


    // get all csv files in Budgeter Directory with the format YYYY-MM.csv
    public ArrayList<String> getDateList() {
        return dateList;
    }

    private void initializeDateList() {
        dateList = new ArrayList<>();
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
        } else {
            System.out.println("Error: Directory does not exist");
        }
    }

    // Get the ExpenseList of the same Month and Year of the given LocalDate.
    // Then, put the ExpenseList in the loadedLists map for later access.
    public ExpenseList getExpenseList(LocalDate monthYear) {
        String dateKey = "";
        dateKey = monthYearString (monthYear);
        if (loadedLists.containsKey(dateKey)) {
            return loadedLists.get(dateKey);
        } else {
            ExpenseList newList = new ExpenseList();
            String filename = dateKey + ".csv";
            newList.loadFromCSV(filename);
            loadedLists.put(dateKey, newList);
            System.out.println("Expense List Loaded " + dateKey);
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
            // If list is now empty, then delete the associated CSV file
            // remove from dateList, and remove from loadedLists
            if (list.isEmpty()) {
                System.out.println("List empty, deleting file for date: " + dateKey);
                deleteFile(new File(directory, dateKey + ".csv"));
                dateList.remove(dateKey);
            } else {
                filename = dateKey + ".csv";
                list.confirmSave(confirm,  filename);
            } 
        }
    }

    // Add new expense to ExpenseList of the correct month
    public void add( Expense newExpense ) {
        ExpenseList list = getExpenseList(newExpense.getLocalDate());
        list.add(newExpense);
        String listDate = list.getMonthYear();
        if (!dateList.contains(listDate)) {
            ListIterator<String> iterator = dateList.listIterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (listDate.compareToIgnoreCase(next) < 0) {
                    iterator.previous();
                    break;
                }
            }
            iterator.add(listDate);
        }
    }

    // Add new expense to ExpenseList of the correct month
    public void add ( String name, String category, LocalDate localDate, double amount) { 
        Expense newExpense = new Expense(name, category, localDate, amount);
        ExpenseList list = getExpenseList(newExpense.getLocalDate());
        list.add(newExpense);
        String listDate = list.getMonthYear();
        if (!dateList.contains(listDate)) {
            ListIterator<String> iterator = dateList.listIterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (listDate.compareToIgnoreCase(next) < 0) {
                    iterator.previous();
                    break;
                }
            }
            iterator.add(listDate);
        }
    }

    // Take an Expense, find it, then replace it with another Expense.
    // If the Expense is successfully replaced, return true.
    public boolean edit( Expense old, Expense updated) {
        boolean foundOldExpense;
        if (old.getLocalDate().getMonthValue() == updated.getLocalDate().getMonthValue() &&
            old.getLocalDate().getYear() == updated.getLocalDate().getYear()) 
        {
            foundOldExpense = getExpenseList(updated.getLocalDate()).edit(old, updated);
        } else {
            foundOldExpense = remove(old);
            if (foundOldExpense) {
                add(updated);
            }
        }
        return foundOldExpense;
    }

    // Remove the given Expense from the month ExpenseList given by the Expense's date.
    public boolean remove( Expense expense ) {
        ExpenseList list = getExpenseList(expense.getLocalDate());
        return list.remove(expense); 
    }

    private boolean deleteFile (File filePath) {
        // Make sure the directory exists
        if (filePath.exists()) {
            // Delete file. Return true if delete successful
            boolean success = filePath.delete();
            if (success) {
                System.out.println("Delete file successful. Astract Pathname: " + filePath.toString());
                return true;
            } else {
                System.out.println("Delete failed. Astract Pathname: " + filePath.toString());
                return false;
            }
        } else {
            System.out.println("Delete failed: File does not exist. Abstract pathname: " + filePath.toString());
            return false;
        }
    }

    // Get month and year String from given LocalDate in format "YYYY-MM"
    public String monthYearString (LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        String s = "-";
        if (month < 10) {
            s = "-0";
        }
        return year + s + month;
    }

}