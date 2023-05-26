package com.application.budgeter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;

public class BudgetModel {
    private ObservableList<Income> incomeList = FXCollections.observableArrayList();
    private ObservableList<Budget> budgetList = FXCollections.observableArrayList();
    
    private double totalIncome;
    private double totalSpent;
    private double totalRemaining;

    public double getTotalSpent() {
        return totalSpent;
    }

    public double getTotalRemaining() {
        return totalRemaining;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

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

    public ObservableList<Income> getIncomeList() {
        return incomeList;
    }

    public ObservableList<Budget> getBudgetList() {
        return budgetList;
    }


    public void addIncome(String source, double incomeAmount) {
        totalIncome += incomeAmount;
        incomeList.add(new Income(source, incomeAmount));
    }

    public void addBudget(String category, double spent, double total) {
        totalSpent += spent;
        totalRemaining = totalIncome - totalSpent;
        budgetList.add(new Budget(category, spent, total));
    }

    public void deleteIncome(Income income) {
        totalIncome -= income.getAmount();
        incomeList.remove(income);
    }

    public void deleteBudget(Budget budget) {
        totalSpent -= budget.getSpent();
        totalRemaining = totalIncome - totalSpent;
        budgetList.remove(budget);
    }

    

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
    

    //Note that when you write to a file in the resources directory, the file will be overwritten each time you run the program. 
    //If you want to persist the data across runs, you may want to consider writing the file to a separate directory such as the user's home directory.
    public void writeCSV(String filename) {
        try {
            PrintStream writer = new PrintStream(new File(filename));
            
            writer.println("Category,Spent,Total");
            for (Budget budget : budgetList) {
                // category, budget, current remainging
                writer.println(budget.getCategory() + "," + budget.getTotal() + "," + budget.getSpent());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        } // end of catch
    } // end of writeCSV method
    

    public void readCSV(String filename) { 
        try {
            Scanner scanner = new Scanner(new File(filename));
            scanner.nextLine(); // skip expense header row
            
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); 
                String[] fields = line.split(","); // create array of fields split by comma

                // add fields to budget
                String category = fields[0].trim();
                double total = Double.parseDouble(fields[1].trim());
                double spent = Double.parseDouble(fields[2].trim());

                this.addBudget(category, spent, total); // add budget to list
            }  // end of while loop
            
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        }
    } // end of readCSV method 
    
    
    
    
    
    //for testing purposes
    public void print2() {
        System.out.println(totalSpent);
        System.out.println(totalIncome);
        System.out.println(totalRemaining);
        
        
    }
    
    public void print() {
        System.out.println(totalSpent);
        System.out.println(totalIncome);
        for (Income income : incomeList) {
            System.out.println(income.getSource() + " - amount: " + income.getAmount());
        }
        for (Budget budget : budgetList) {
            System.out.println(budget.getCategory() + " - total: $" + budget.getTotal() + " - Spent: $" + budget.getSpent() + ", Remaining: $" + budget.getRemaining());
        }
    }
}
