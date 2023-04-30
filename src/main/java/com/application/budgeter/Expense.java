package com.application.budgeter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

// This class defines an object that holds information about an expense entry
public class Expense {
    private int ID;
    private String name;
    private LocalDate localDate;
    private String category;
    private int amount;

    
    public Expense (int ID, String name, LocalDate localDate, String category, int amount) {
        this.ID = ID;
        this.name = name;
        this.localDate =localDate;
        this.category = category;
        this.amount = amount;
    }

    public boolean equals ( Expense other) {
        return other.getID() == this.ID 
            && other.getName().equals(this.name)
            && other.getLocalDate().equals(this.localDate)
            && other.getCategory().equals(this.category)
            && other.getAmount() == this.amount;
    }

    public int getID() { return ID; } 

    public String getName() { return name; } 

    public LocalDate getLocalDate() { return localDate; } 

    public String getCategory() { return category; } 

    public int getAmount() { return amount; } 

    public void setID(int ID) { this.ID= ID; } 

    public void setName(String name) { this.name = name; } 

    public void setLocalDate(LocalDate localDate) { this.localDate = localDate; } 

    public void setCategory(String category) { this.category = category; } 

    public void setAmount(int amount) { this.amount= amount; }
}