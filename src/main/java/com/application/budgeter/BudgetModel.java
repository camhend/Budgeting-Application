package com.application.budgeter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BudgetModel {
    private ObservableList<Income> incomeList = FXCollections.observableArrayList();
    private ObservableList<Budget> budgetList = FXCollections.observableArrayList();
    
    private double totalIncome;
    private double totalSpent;
    private double totalRemaining;


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
            File file = new File(System.getProperty("user.home"), filename);
            FileWriter writer = new FileWriter(file);
            writer.write("Category,Spent,Total\n"); // write expense header row
            for (Budget budget : budgetList) {
                writer.write(budget.getCategory() + "," + budget.getSpent() + "," + budget.getTotal() + "\n");
            }
            writer.write("\nSource,Amount\n"); // write income header row
            for (Income income : incomeList) {
                writer.write(income.getSource() + "," + income.getAmount() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        } // end of catch
    } // end of writeCSV method
    
    public void readCSV(String filename) { 
        try {
            File file = new File(System.getProperty("user.home"), filename);
            Scanner scanner = new Scanner(file);
            scanner.nextLine(); // skip header row
            while (scanner.hasNextLine()) { 
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                if (fields.length == 2) { // income data
                    String source = fields[0].trim();
                    String amountString = fields[1].trim();
                    if (!amountString.matches("\\d+(\\.\\d+)?")) { // check if amountString contains only digits and optionally a decimal point
                        continue; // skip to next line
                    }
                    double amount = Double.parseDouble(amountString);
                    Income income = new Income(source, amount);
                    incomeList.add(income);
                } else if (fields.length == 3) { // expense data
                    String category = fields[0].trim();
                    String amountString = fields[1].trim();
                    if (!amountString.matches("\\d+(\\.\\d+) ?")) { // check if amountString contains only digits and optionally a decimal point
                        continue; // skip to next line
                    }
                    double spent = Double.parseDouble(amountString);
                    double total = Double.parseDouble(fields[2].trim());
                    Budget budget = new Budget(category, spent, total);
                    budgetList.add(budget);
                } // end of if statement
            } // end of while loop
            scanner.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } // end of catch
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
