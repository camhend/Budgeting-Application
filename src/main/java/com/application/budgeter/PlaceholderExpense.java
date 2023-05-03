package com.application.budgeter;


// placeholder expense class for ExpenseController

public class PlaceholderExpense 
{
    String expense;
    String category;
    String date;
    String cost;

    public PlaceholderExpense(String expense, String category, String date, String cost) {
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

    // set methods used by ExpenseController
    public void setExpense(String expense) {
        this.expense = expense;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
 