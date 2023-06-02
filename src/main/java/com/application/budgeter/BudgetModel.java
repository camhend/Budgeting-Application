package com.application.budgeter;

import java.io.File;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class BudgetModel {

    private Map<String, BudgetList> loadedLists;

    public BudgetModel() {
        this.loadedLists = new HashMap<>();
    } // end of constructor


    // get list of dates of all budget lists
    public ArrayList<String> getDateList() {
        ArrayList<String> dateList = new ArrayList<>();

        // get path of budgetdata folder
        String projectRootPath = System.getProperty("user.dir") + "\\budgetdata";
        File directory = new File(projectRootPath);

        // Make sure the directory exists
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            // get list of dates of all budget lists .csv files
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
    } // end getDateList method


    // get budget list from csv file of a month 
    public BudgetList getBudgetList(LocalDate monthYear) {
        String dateKey = "";
        if (monthYear.getMonthValue() > 9) {
            dateKey = monthYear.getYear() + "-" + monthYear.getMonthValue();
        } else {
            dateKey = monthYear.getYear() + "-0" + monthYear.getMonthValue();
        }
        if (loadedLists.containsKey(dateKey)) {
            return loadedLists.get(dateKey);
        } else {
            BudgetList newList = new BudgetList(monthYear);
            String filename = dateKey + ".csv";
            newList.readCSV(filename);
            loadedLists.put(dateKey, newList);
            System.out.println("Budget Loaded " + dateKey + ".csv");

            return newList;
        }
    } // end of getBudgetList method


    // update spending of each category for a month's budgetlist with the category spending from the expense list
    public void updateSpentField(ExpenseModel expenseModel, LocalDate monthYear) {
        // get budget list and expense list of selected month
        BudgetList budgetList = this.getBudgetList(monthYear);
        ExpenseList expenseList = expenseModel.getExpenseList(monthYear);

        // update spent field for each budget category with the category spending from the expense list
        for (Budget budget : budgetList.getBudgetList()) {
            double spent = expenseList.getCategorySpending(budget.getCategory());
            if (spent == -1) { spent = 0; } // if category not found in expense list, set spent to 0

            budget.setSpent(spent);
        }
    } // end of updateSpentField method


    // save all loaded budget lists to csv files
    public void saveAll() {
        String filename;
        for (String dateKey : loadedLists.keySet()) {
            BudgetList list = loadedLists.get(dateKey);
            filename = dateKey + ".csv";
 
            list.writeCSV(filename);
        }
    } // end of saveAll method
} // end of BudgetModel class