package com.application.budgeter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BudgetModel {
    private ArrayList<Income> incomeList = new ArrayList<Income>();
    private ArrayList<Budget> budgetList = new ArrayList<Budget>();
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

    //can figure this out later 
    public void updateTotalSpending() {
        double totalSpent = 0.0;
        for (Budget budget : budgetList) {
            totalSpent += budget.getSpent();
        }
        //.setText(String.format("$%.2f", totalSpent));
        
        totalRemaining = totalIncome - totalSpent;
        //.setText(String.format("$%.2f", totalRemaining));
        
        double percentageSpent = totalSpent / totalIncome * 100.0;
        //.setText(String.format("%.1f%%", percentageSpent));
        
        double percentageRemaining = totalRemaining / totalIncome * 100.0;
        //.setText(String.format("%.1f%%", percentageRemaining));
        
        // Update the progress bar with the percentage remaining
        double percentageComplete = 100.0 - percentageSpent;
        //progressBar.setProgress(percentageComplete / 100.0);
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
        }
    }
    
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
                }
            }
            scanner.close();
            updateTotalSpending(); // Update the total spending values after reading the CSV file
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    public void editIncome() {

    }
    
    public void deleteIncome() {
        
    }

    public void editBudget() {

    }
    
    public void deleteBudget() {

    }

    
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
    //read csv

    //addExpense
    //deleteIncome
    //deleteExpense
    //updateTotalSpending?

}
