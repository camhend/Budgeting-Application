package com.application.budgeter;

public class Budget {
    String category;
    double spent;
    double remaining;
    double total; 

    public Budget(String category, double spent, double total) {
        remaining = total-spent;
        this.category = category;
        this.spent = spent;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public double getSpent() {
        return spent;
    }

    public double getRemaining() {
        return remaining;
    }

    public double getTotal() {
        return total;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSpent(double spent) {
        remaining = total-spent;
        this.spent = spent;
    }

    public void setTotal(double total) {
        remaining = total-spent;
        this.total = total;
    }
    
}
