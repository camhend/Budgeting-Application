package com.application.budgeter;

public class Maintest {
    
    public static void main(String[] args) {
        
        BudgetModel b = new BudgetModel();
       /* 
         b.addIncome("work", 50);
        b.print();
        
        b.addIncome("chores", 50);
        b.print();
        
        b.addIncome("stealing", 3900);
        b.print();
    
        b.addBudget("rent", 1150, 1150);
        b.print2();

        b.addBudget("gas", 126, 140);
        b.print2();
        b.addBudget("utilities", 65, 200);
        b.print2();
        b.writeCSV("test.csv");*/
        
       b.readCSV("test.csv");
        b.print();
    
    }
}
