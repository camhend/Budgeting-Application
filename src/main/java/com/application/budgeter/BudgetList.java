package com.application.budgeter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.io.PrintStream;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;


public class BudgetList {
    
    private ObservableList<Budget> budgetList = FXCollections.observableArrayList();
    
    private double totalIncome; // total of all amounts of all budgets
    private double totalSpent; // total spent of all budgets
    private double totalRemaining; // total remaining of all budgets
    private LocalDate monthYear; // localdate of month and year of budgetlist


    public BudgetList() {
    } // end constructor

    public BudgetList(LocalDate monthYear) {
        this.monthYear = monthYear;
    } // end constructor


    // get categories from budgetlist of selected month
    public ArrayList<String> getCategoryList() {
        ArrayList<String> categoryList = new ArrayList<String>();
        // creates list of unique categories
        for (Budget budget : budgetList) {
            if (!categoryList.contains(budget.getCategory())) {
                categoryList.add(budget.getCategory());
            }
        }
        return categoryList;
    } // end getCategoryList method


    
    // getters

    public double getTotalSpent() {
        return totalSpent;
    }

    public double getTotalRemaining() {
        return totalRemaining;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public ObservableList<Budget> getBudgetList() {
        return budgetList;
    }

    public LocalDate getMonthYear() {
        return monthYear;
    }



    // data manipulation methods

    public void add(Budget budget) {
        // update totals
        totalIncome += budget.getTotal();
        totalSpent += budget.getSpent();
        totalRemaining = totalIncome - totalSpent;

        budgetList.add(budget);
    } // end add method


    public void remove(Budget budget) {
        // update totals
        totalIncome -= budget.getTotal();
        totalSpent -= budget.getSpent();
        totalRemaining = totalIncome - totalSpent;

        budgetList.remove(budget);
    } // end delete method

    public void clear() {
        budgetList.clear();
        totalIncome = 0;
        totalSpent = 0;
        totalRemaining = 0;
    } // end clear method


    public void edit(Budget oldBudget, Budget newBudget) {
        // remove old budget data
        totalIncome -= oldBudget.getTotal();
        totalSpent -= oldBudget.getSpent();

        // add new budget data
        totalIncome += newBudget.getTotal();
        totalSpent += newBudget.getSpent();

        // update total
        totalRemaining = totalIncome - totalSpent;

        // replace old budget with new budget
        budgetList.remove(oldBudget);
        budgetList.add(newBudget);
    } // end edit method



    // File I/O methods

    public boolean writeCSV(String filename) {
        try {
            // 1. create writer of file
            String projectRootPath = System.getProperty("user.dir") + "\\budgetdata";
            File file = new File(projectRootPath + "\\" + filename);
            PrintStream writer = new PrintStream(file);

            // 2. print data 
            writer.println("Category,Total,Spent");
            for (Budget budget : budgetList) {
                // writecategory, total, spent
                String category = budget.getCategory();
                String total = String.format("%.2f", budget.getTotal());
                String spent = String.format("%.2f", budget.getSpent());
                writer.println(category + "," + total + "," + spent);
            }
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    } // end of writeCSV method
    

    // clear budgetList and add budgets from read from CSV file
    public boolean readCSV(String filename) { 
        try {
            // 1. clear budgetList
            budgetList.clear();

            // 2. create reader of file
            String projectRootPath = System.getProperty("user.dir") + "\\budgetdata";
            File file = new File(projectRootPath + "\\" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));

            // 3. read data
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(","); // create array of fields split by comma
                // add fields to expense
                String category = fields[0]; 

                // get string amount and convert to double
                String amount = fields[1];
                double amountDouble = Double.parseDouble(amount);

                // get string spent and convert to double
                String spent = fields[2];
                double spentDouble = Double.parseDouble(spent);

                // add
                Budget budget = new Budget(category, spentDouble, amountDouble);
                
                this.add(budget);
                line = reader.readLine();
            }
            reader.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    } // end of readCSV method 
} // end of BudgetList class