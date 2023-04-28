package com.application.budgeter;


// placeholder expense class for ExpenseController

public class Expense 
{
    String expense;
    String category;
    String date;
    String cost;

    public Expense(String expense, String category, String date, String cost) {
        this.expense = expense;
        this.category = category;
        this.date = date;
        this.cost = cost;
    }

    // get methods used by ExpenseController
    public String getExpense() {
        return expense;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getCost() {
        return cost;
    }
}
 