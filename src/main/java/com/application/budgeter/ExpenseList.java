package com.application.budgeter;

import java.util.Scanner;
import java.io.File;
import java.time.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.collections.ModifiableObservableListBase;


// singleton class to hold all expenses (makes it easier to access from different controllers)
public class ExpenseList extends ModifiableObservableListBase<Expense>{

    // private class to define a node in the ExpenseList
    private static class ExpenseNode {
        public ExpenseNode next;
        public ExpenseNode prev;
        public Expense expense;
    
        public ExpenseNode (Expense expense, ExpenseNode next, ExpenseNode prev) {
            this.next = next;
            this.prev = prev;
            this.expense = expense;
        }
    }

    // singleton field used to access the ExpenseList
    private static ExpenseList instance = null;
    private ExpenseNode head;
    private ExpenseNode tail;
    private int size;

    // private constructor to prevent instantiation
    private ExpenseList() {
        head = null;
        tail = null;
        size = 0;
    }

    // singleton method to access the ExpenseList
    public static ExpenseList getInstance() {
        // if instance is null, create a new ExpenseList
        if (instance == null) {
            instance = new ExpenseList();
        }
        return instance;
    }



    // TODO: keep track of spending in a field. Add() and remove() edit the value

    /* 
    * DO NOT USE?
    * 
    // add to end of list with given ID
    public void add ( int ID, LocalDate localDate, String category, int amount  ) {
        Expense expense = new Expense( ID, localDate, category, amount);
        ExpenseNode node = new ExpenseNode (expense, null, null);
        if ( this.isEmpty() ) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }
    */


    
    // add to end of list. Increment ID from current tail node's expense ID
    public void add (  String name, String category, LocalDate localDate, double amount  ) {
        int ID;
        
        if (this.isEmpty() ) {ID = 1;} // if first expense, set ID to 1
        else {ID = tail.expense.getID() + 1;} // else, set ID to a new unique ID 

        Expense expense = new Expense( ID, name, localDate, category, amount);
        ExpenseNode node = new ExpenseNode (expense, null, null);

        // add node to end of list
        if ( this.isEmpty() ) { // if list is empty, set head and tail to node
            head = node;
            tail = node;
        } else { // else, add node to end of list
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    public void doAdd(int index, Expense expense) {
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException();
        }
        int ID;
        
        if (this.isEmpty() ) {ID = 1;} // if first expense, set ID to 1
        else {ID = tail.expense.getID() + 1;} // else, set ID to a new unique ID 

        expense.setID(ID);
        ExpenseNode node = new ExpenseNode (expense, null, null);

        if (index == 0) {
            node.next = head;
            head.prev = node;
            head = node;
        } else if (index == this.size()) {
            tail.next = node;
            node.prev = tail;
            tail = node;
        } else {
            ExpenseNode current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            node.next = current;
            node.prev = current.prev;
            current.prev.next = node;
            current.prev = node;
        }
        size++;
    }

    // get the ExpenseNode that contains the given Expense
    private ExpenseNode getNode( Expense other) {
        ExpenseNode current = head;
        while (current != null) {
            if ( current.expense.equals(other) ) {
                return current;
            } else {
                current = current.next;
            }
        }
        return null;
    }

    public Expense get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        ExpenseNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.expense;
    }

    // Remove the ExpenseNode that contains the given Expense
    public void removeNode( Expense target ) {
        ExpenseNode node = this.getNode(target);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public Expense doRemove( int index ) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        ExpenseNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.prev.next = current.next;
        current.next.prev = current.prev;
        size--;
        return current.expense;
    }

    public void edit( Expense target, String name, String category, LocalDate localDate, double amount ) {
        ExpenseNode node = this.getNode(target);
        node.expense.setName(name);
        node.expense.setCategory(category);
        node.expense.setLocalDate(localDate);
        node.expense.setAmount(amount);
    }

    public Expense doSet( int index, Expense expense) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        ExpenseNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        Expense oldExpense = current.expense;
        current.expense = expense;
        return oldExpense;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }


    // loads expenses from expenses.csv returns true if successful, false if not
    public boolean loadExpenses() {
        // read expenses.csv file and add to list
        try {
            // create new scanner for file
            Scanner scanner = new Scanner(new File("expenses.csv"));
            // create a new expense out of each line
            while (scanner.hasNextLine()) {
                // create new scanner for line
                Scanner line = new Scanner(scanner.nextLine());
                // use , as delimiter
                line.useDelimiter(","); // separate by commas

                String expense = line.next();
                String category = line.next();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate date = LocalDate.parse(line.next(), formatter);
                double cost = Double.parseDouble(line.next());

                // add new PlaceholderExpense to list
                this.add(expense, category, date, cost);
                // close line scanner
                line.close();
            }
            // close scanner
            scanner.close();
            // return true if successful
            return true;
        // catch if file is not found
        } catch (FileNotFoundException e) {
            return false; // return false if unsuccessful
        }
    }


    // save data from tableview to file
    public boolean saveExpenses() {
        // write list to expenses.csv
        try {
            // open file and write each expense to file
            FileWriter csvWriter = new FileWriter("expenses.csv");
            // create new reference to head and iterate through list
            ExpenseNode current = head;
            while (current != null) {
                // write expense to file
                csvWriter.append(current.expense.getName());
                csvWriter.append(",");
                csvWriter.append(current.expense.getCategory());
                csvWriter.append(",");
                csvWriter.append(current.expense.getLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString());
                csvWriter.append(",");
                csvWriter.append(String.format("%.2f", current.expense.getAmount()));
                csvWriter.append("\n");
                // move to next node
                current = current.next;
            }

            csvWriter.flush(); // flush data to file
            csvWriter.close(); // close file

            // return true if successful
            return true;
        }
        catch (IOException e) {
            // return false if unsuccessful
            return false;
        }
    } // end saveExpenses 



    public int calcluateTotal(int months) {
        // create new reference to head and iterate through list
        ExpenseNode current = head;
        int total = 0;
        while (current != null) {
            // if expense is within the last month, add to total
            if (current.expense.getLocalDate().isAfter(LocalDate.now().minusMonths(months))) {
                total += current.expense.getAmount();
            }
            // move to next node
            current = current.next;
        }
        return total;
    }



    // *****************************************EXPERIMENT METHOD NOT TESTED**********************************************
    // gives total spent in this calendar month (within the actual month,)
    public int calcluateThisMonth() {
        // create new reference to head and iterate through list
        ExpenseNode current = head;
        int total = 0;
        while (current != null) {
            // if expense is within the last calendar month, add to total
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1); // start of this month
            LocalDate endOfMonth = LocalDate.now().withDayOfMonth(1); // end of this month
            // if expense is within the last month, add to total
            if (current.expense.getLocalDate().isAfter(startOfMonth) && current.expense.getLocalDate().isBefore(endOfMonth)) {
                total += current.expense.getAmount();
            }
            // move to next node
            current = current.next;
        }
        return total; 
    }



    // implement iterator? https://www.geeksforgeeks.org/java-implementing-iterator-and-iterable-interface/
    
}
