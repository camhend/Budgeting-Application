package com.application.budgeter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

// This class defines an object that holds information about an expense entry
public class Expense {
    private String name;
    private LocalDate localDate;
    private String category;
    private int amount;

    
    public Expense (String name, String category, LocalDate localDate, int amount) {
        this.name = name;
        this.category = category;
        this.localDate =localDate;
        this.amount = amount;
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

    public int getAmount() { return amount; } 

    public void setName(String name) { this.name = name; } 

    public void setCategory(String category) { this.category = category; } 

    public void setLocalDate(LocalDate localDate) { this.localDate = localDate; } 

    public void setAmount(int amount) { this.amount= amount; }
}