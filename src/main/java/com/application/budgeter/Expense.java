package com.application.budgeter;

import java.time.*;
import java.util.Comparator;

// This class defines an object that holds information about an expense entry
public class Expense {
    private String name;
    private LocalDate localDate;
    private String category;
    private double amount;

    
    public Expense (String name, String category, LocalDate localDate, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Expense amount cannot be negative.");
        } else {
            this.amount = amount;
        }
        this.name = name;
        this.category = category;
        this.localDate =localDate;   
    }

    public boolean equals ( Expense other) {
        return other.getName().equals(this.name)    
            && other.getCategory().equals(this.category)
            && other.getLocalDate().equals(this.localDate)
            && other.getAmount() == this.amount;
    }

    public String getName() { return name; } 

    public String getCategory() { return category; } 
    
    public LocalDate getLocalDate() { return localDate; } 

    public double getAmount() { return amount; } 

    public void setName(String name) { this.name = name; } 

    public void setCategory(String category) { this.category = category; } 

    public void setLocalDate(LocalDate localDate) { this.localDate = localDate; } 

    public void setAmount(double amount) { 
        if (amount < 0) {
            throw new IllegalArgumentException("Expense amount cannot be negative.");
        } else {
            this.amount = amount;
        }
    }

    // Return a cloned version of this Expense object
    public Expense copy () {
        return new Expense(this.name, this.category, this.localDate, this.amount);
    }

    public String toString () {
        String toString = 
            "[" + 
            name + ", " +
            category + ", " +
            localDate.toString() + ", " +
            amount + "]";
        return toString;
    }

}

class LexicographicNameComparitor implements Comparator<Expense> {
    @Override
    public int compare(Expense a, Expense b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}

class LexicographicCategoryComparitor implements Comparator<Expense> {
    @Override
    public int compare(Expense a, Expense b) {
        return a.getCategory().compareToIgnoreCase(b.getCategory());
    }
}

class DateComparitor implements Comparator<Expense> {
    @Override
    public int compare(Expense a, Expense b) {
        return a.getLocalDate().compareTo(b.getLocalDate());
    }
}

class AmountComparitor implements Comparator<Expense> {
    @Override
    public int compare(Expense a, Expense b) {
        return Double.compare(a.getAmount(), b.getAmount());
    }
}