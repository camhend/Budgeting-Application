package com.application.budgeter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.FileReader;




public class BudgetModel {
    private Map<String, ObservableList<Budget>> loadedLists;

    private ObservableList<Budget> budgetList = FXCollections.observableArrayList();
    
    private double totalIncome;
    private double totalSpent;
    private double totalRemaining;


    public BudgetModel() {
        this.loadedLists = new HashMap<>();
    } // end constructor



    public ArrayList<String> getDateList() {
        ArrayList<String> dateList = new ArrayList<>();

        String projectRootPath = System.getProperty("user.dir") + "\\budgetdata";
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
    } // end getDateList method



    // get budget list from csv file of a month 
    public ObservableList<Budget> getBudgetList(LocalDate monthYear) {
        String dateKey = "";
        if (monthYear.getMonthValue() > 9) {
            dateKey = monthYear.getYear() + "-" + monthYear.getMonthValue();
        } else {
            dateKey = monthYear.getYear() + "-0" + monthYear.getMonthValue();
        }
        if (loadedLists.containsKey(dateKey)) {
            return loadedLists.get(dateKey);
        } else {
            ObservableList<Budget> newList = FXCollections.observableArrayList();
            String filename = dateKey + ".csv";
            newList = this.returnReadCSV(filename);
            loadedLists.put(dateKey, newList);
            System.out.println("Loaded " + dateKey + ".csv");

            return newList;
        }
    }

   
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
    }



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

    public void setBudgetList(ObservableList<Budget> budgetList) {
        this.budgetList = budgetList;
    }



    // add methods

    public void addIncome(String source, double incomeAmount) {
        totalIncome += incomeAmount;
    }

    public void addBudget(String category, double spent, double total) {
        totalSpent += spent;
        totalRemaining = totalIncome - totalSpent;
        budgetList.add(new Budget(category, spent, total));
    }

    public void deleteIncome(Income income) {
        totalIncome -= income.getAmount();
    }

    public void deleteBudget(Budget budget) {
        totalSpent -= budget.getSpent();
        totalRemaining = totalIncome - totalSpent;
        budgetList.remove(budget);
    }


    
    // edit methods

    public void editIncomeCategory(Income income, String newSource) {
        income.setSource(newSource);
    }

    public void editIncomeAmount(Income income, double newAmount) {
        income.setAmount(newAmount);
    }
    
    public void editBudgetCategory(Budget budget, String newCategory) {
        budget.setCategory(newCategory);
    }

    public void editBudgetSpent(Budget budget, double newSpent) {
        budget.setSpent(newSpent);
    }

    public void editBudgetTotal(Budget budget, double newTotal) {
        budget.setTotal(newTotal);
    }
    


    // file I/O methods


    public void saveAll(boolean confirm) {
        String filename;
        ObservableList<Budget> currList = this.getBudgetList();
        for (String dateKey : loadedLists.keySet()) {
            ObservableList<Budget> list = loadedLists.get(dateKey);
            filename = dateKey + ".csv";
 
            // set list to list of month
            this.setBudgetList(list);
            writeCSV(filename);
        }
        this.setBudgetList(currList);
    }
    

    public boolean writeCSV(String filename) {
        try {
            // create writer of file
            String projectRootPath = System.getProperty("user.dir") + "\\budgetdata";
            File directory = new File(projectRootPath);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(projectRootPath + "\\" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintStream writer = new PrintStream(file);

            // print writer added
            writer.println("Category,Spent,Total");
            for (Budget budget : budgetList) {
                // category, total, spent
                writer.println(budget.getCategory() + "," + budget.getTotal() + "," + budget.getSpent());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    } // end of writeCSV method
    

    public boolean readCSV(String filename) { 
        try {
            // create reader of file
            String projectRootPath = System.getProperty("user.dir") + "\\budgetdata";
            File directory = new File(projectRootPath);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(projectRootPath + "\\" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // print reader added
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(","); // create array of fields split by comma
                // add fields to expense
                String category = fields[0]; 

                // convert string to double
                String amount = fields[1];
                double amountDouble = Double.parseDouble(amount);

                // convert string to double
                String spent = fields[2];
                double spentDouble = Double.parseDouble(spent);
                
                this.addBudget(category, spentDouble, amountDouble);
                line = reader.readLine();
            }
            reader.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    } // end of readCSV method 


    public ObservableList<Budget> returnReadCSV(String filename) { 
        ObservableList<Budget> budgetList = FXCollections.observableArrayList();

        try {
            // create reader of file
            String projectRootPath = System.getProperty("user.dir") + "\\budgetdata";
            File directory = new File(projectRootPath);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(projectRootPath + "\\" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // print reader added
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(","); // create array of fields split by comma
                // add fields to expense
                String category = fields[0]; 

                // convert string to double
                String amount = fields[1];
                double amountDouble = Double.parseDouble(amount);

                // convert string to double
                String spent = fields[2];
                double spentDouble = Double.parseDouble(spent);

                Budget budget = new Budget(category, spentDouble, amountDouble);
                
                budgetList.add(budget);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
        }
        return budgetList;
    } // end of readCSV method 
    

    
    // testing methods

    public void print2() {
        System.out.println(totalSpent);
        System.out.println(totalIncome);
        System.out.println(totalRemaining);  
    } // end of print method
    
    public void print() {
        System.out.println(totalSpent);
        System.out.println(totalIncome);
        for (Budget budget : budgetList) {
            System.out.println(budget.getCategory() + " - total: $" + budget.getTotal() + " - Spent: $" + budget.getSpent() + ", Remaining: $" + budget.getRemaining());
        }
    } // end of print method
} // end of BudgetModel class