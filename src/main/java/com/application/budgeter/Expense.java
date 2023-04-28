package com.application.budgeter;

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
 