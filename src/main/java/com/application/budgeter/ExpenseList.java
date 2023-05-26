package com.application.budgeter;

import java.util.Iterator;
import java.time.*;
import java.util.*;
import java.io.*;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;

// TODO: make CSV reader / writer
    // consider: how many expense to load? 
    // how far back to go?
    // How to add newly added expenses to CSV log? 
    // What if a really old date is added? How will the newly added date be added to the CSV in a sorted manner?

// TODO: what happens if an expense is edited to have a very old date?

// TODO: what happens if an expense is edited to have a very old date?

// This class defines a LinkedList used for storing
// Expense objects. Oldest items are at the head, newest at the tail.
public class ExpenseList implements Iterable<Expense> {
    private ExpenseNode head;
    private ExpenseNode tail;
    private double totalSpending;
    private int size;
    private Map<String, Double> categorySpending;
    private ExpenseList copy; // copy of current list used for reverting changes
    //private final LocalDate monthYear;

    // ExpenseList constructor
    public ExpenseList () {
        this.head  = null;
        this.tail = null;
        this.size = 0;
        this.totalSpending = 0;
        this.categorySpending = new HashMap<String, Double>();
        this.copy = null;
    } 

    // Nested class for LinkedList Nodes
    public static class ExpenseNode {
        public ExpenseNode next;
        public ExpenseNode prev;
        public Expense expense;

        public ExpenseNode (Expense expense, ExpenseNode next, ExpenseNode prev) {
            this.next = next;
            this.prev = prev;
            this.expense = expense;
        }
    }

    // return month and year (yyyy-mm)
    public String getMonthYear() {
        if (head == null) {
            return null;
        }
        return head.expense.getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
    
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }
    

    public double getTotalSpending() {
        return totalSpending;
    }

    // Get total spending for a given category. Matches word exactly.
    // If the category isn't found, then returns negative number.
    public double getCategorySpending ( String category ) {
        try {
            return categorySpending.get(category);
        } catch (NullPointerException e) {
            return -1;
        }
        
    }


    // Add new Expense to the list in sorted order by date
    // Takes Expense fields as parameters
    public void add ( String name, String category, LocalDate localDate, double amount) { 
        // if backup copy wasn't made, create it now
        if (this.copy == null) {
            this.copy = this.copy();
        }        
        Expense newExpense = new Expense( name, category, localDate, amount);
        ExpenseNode newNode = new ExpenseNode (newExpense, null, null);
        // List is empty
        if ( this.isEmpty() ) { 
            head = newNode;
            tail = newNode;
        // New expense is equal or after the tail's date; add to tail
        } else if ( newExpense.getLocalDate().isEqual(tail.expense.getLocalDate()) ||
                    newExpense.getLocalDate().isAfter(tail.expense.getLocalDate()) ) {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        // New expense is before the head's date; add to head
        } else if ( newExpense.getLocalDate().isBefore(head.expense.getLocalDate()) ) {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        // Else, search for sorted spot to add
        } else {
            ExpenseNode current = tail;
            while ( newExpense.getLocalDate().isBefore(current.expense.getLocalDate()) ) {
                current = current.prev;
            }
            current.next.prev = newNode;
            newNode.next = current.next;
            newNode.prev = current;
            current.next = newNode;           
        }
        totalSpending += newExpense.getAmount();
        size++;
        if (categorySpending.containsKey(category)) {
            categorySpending.replace(category, categorySpending.get(category) + amount);
        } else {
            categorySpending.put(category, amount);
        }
    }

    // Add new Expense to list in sorted order by date
    // Takes Expense object as parameter
    public void add ( Expense newExpense) { 
        // if backup copy wasn't made, create it now
        if (this.copy == null) {
            this.copy = this.copy();
        }  
        ExpenseNode newNode = new ExpenseNode (newExpense, null, null);
        // List is empty
        if ( this.isEmpty() ) {
            head = newNode;
            tail = newNode;
        // New expense is equal or after the tail's date; add to tail
        } else if ( newExpense.getLocalDate().isEqual(tail.expense.getLocalDate()) ||
                    newExpense.getLocalDate().isAfter(tail.expense.getLocalDate()) ) {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        // New expense is before the head's date; add to head
        } else if ( newExpense.getLocalDate().isBefore(head.expense.getLocalDate()) ) {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        // Else, search for sorted spot to add
        } else {
            ExpenseNode current = tail;
            while ( newExpense.getLocalDate().isBefore(current.expense.getLocalDate()) ) {
                current = current.prev;
            }
            current.next.prev = newNode;
            newNode.next = current.next;
            newNode.prev = current;
            current.next = newNode;           
        }
        totalSpending += newExpense.getAmount();
        size++;
        String category = newExpense.getCategory();
        double amount = newExpense.getAmount();
        if (categorySpending.containsKey(category)) {
            categorySpending.replace(category, categorySpending.get(category) + amount);
        } else {
            categorySpending.put(category, amount);
        }
    }

    // Get the ExpenseNode that contains the given Expense
    private ExpenseNode getNode( Expense other) {
        ExpenseNode current = tail;
        while (current != null) {
            if ( current.expense.equals(other) ) {
                return current;
            } else {
                current = current.prev;
            }
        }
        return null;
    }

    // Get Expense object at the given list index
    public Expense get( int index ) {
        if ( index < 0 || index > size - 1 ) {
            throw new IndexOutOfBoundsException();
        }
        ExpenseNode current;
        if ( index < size / 2 ) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current.expense;
    }

    // Takes an Expense object in the ExpenseList and 
    // replaces it with a new Expense object, and return true.
    // If the date was changed, then move the ExpenseNode to
    // the correct position in the sorted list.
    // If the given Expense is not in the list, then return false.
    public boolean edit( Expense old, Expense updated) {
        // if backup copy wasn't made, create it now
        if (this.copy == null) {
            this.copy = this.copy();
        }
        ExpenseNode node = this.getNode(old);
        if (node == null) { // expense not found in list
            return false; 
        }
        node.expense = updated;
        totalSpending -= old.getAmount() + updated.getAmount();;
        // Remove old Expense from categorySpending map
        categorySpending.replace(old.getCategory(), 
            categorySpending.get(old.getCategory()) - old.getAmount());
        // Add updated Expense to categorySpending map. 
        // Create new mapping if necessary
        if (categorySpending.containsKey(updated.getCategory())) {
            categorySpending.replace(updated.getCategory(), 
                categorySpending.get(updated.getCategory()) + updated.getAmount());
        } else {
            categorySpending.put(updated.getCategory(), updated.getAmount());
        }
        
        // If expense date was changed, then move the node
        // to the correct sorted position.
        if ( !updated.getLocalDate().equals(old.getLocalDate()) ) {
            // First, remove edited node from the current position
            if (node == head && node == tail) {
                // do not move if this node is the only node
                return true;
            } else if (node == head) {
                // If node is head and node should still be the head, 
                // then do not move the node
                if (updated.getLocalDate().isBefore(node.next.expense.getLocalDate()) ) {
                    return true;
                } else {
                    node.next.prev = null;
                    head = node.next;
                }
                // If node is the tail and node should still be the tail, 
                // then do not move the node
            } else if (node == tail) {
                if (updated.getLocalDate().isAfter(node.prev.expense.getLocalDate() )
                    || updated.getLocalDate().equals(node.prev.expense.getLocalDate()) ) {
                    return true;
                }
                node.prev.next = null;
                tail = node.prev;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }

            // If the updated date is BEFORE the old date
            if (updated.getLocalDate().isBefore(old.getLocalDate())) {
                // If updated node is an earlier date than head, 
                // then reassign head to be the updated node
                if (updated.getLocalDate().isBefore(head.expense.getLocalDate())) {
                    node.next = head; // node's next is the old head
                    node.prev = null; // node's prev is null
                    head.prev = node; // old head's prev is the node
                    head = node; // node is the new head
                // else traverse for the correct position
                } else {
                    ExpenseNode current = node.prev; // start at the node before the updated node
                    // go back until updated is before the current node
                    while (updated.getLocalDate().isBefore(current.expense.getLocalDate())) {
                        current = current.prev;
                    }
                    
                    node.next = current.next;
                    node.prev = current;
                    current.next.prev = node;
                    current.next = node;
                }
                
                    
            // If the updated date is AFTER the old date
            } else if (updated.getLocalDate().isAfter(old.getLocalDate())) {
                // If updated node is an later or equal date than tail, 
                // then reassign tail to be the updated node
                if ( updated.getLocalDate().isAfter(tail.expense.getLocalDate()) || 
                     updated.getLocalDate().equals(tail.expense.getLocalDate()) ) 
                {
                    node.next = null;
                    node.prev = tail;
                    tail.next = node;
                    tail = node;                   
                // Else traverse for the correct position
                } else {
                    ExpenseNode current = node;
                    // traverse until the current node date is after the updated date 
                    while ( updated.getLocalDate().isAfter(current.expense.getLocalDate())
                            || updated.getLocalDate().equals(current.expense.getLocalDate()))  {
                        current = current.next;
                    } 
                    node.next = current;
                    node.prev = current.prev;
                    current.prev.next = node;
                    current.prev = node;
                }
            }
        } // End of If expense's date was changed
        return true;
    } // End of edit method

    // Return whether list contains the given Expense
    public boolean contains( Expense expense ) {
        ExpenseNode current = tail;
        while (current != null) {
            if (current.expense.equals( expense )) {
                return true;
            } else {
                current = current.prev;
            }
        }
        return false;
    }

    public int getIndex( Expense expense ) {
        ExpenseNode current = head;
        int index = 0;
        while (current != null) {
            if (current.expense.equals( expense )) {
                return index;
            } else {
                current = current.next;
                index++;
            }
        }
        return -1;
    }


    // clear the list of all elements
    public void clear () {
        this.head = null;
        this.tail = null;
        size = 0;
        totalSpending = 0;
        categorySpending.clear();
    }

    // Remove the ExpenseNode that contains the given Expense
    // Return true if successfully removed.
    public boolean remove( Expense expense ) {
        // if backup copy wasn't made, create it now
        if (this.copy == null) {
            this.copy = this.copy();
        }
        ExpenseNode node = this.getNode(expense);
        if (node == null) {
            return false;
        } else {
            if (node == head && node == tail) {
                head = null;
                tail = null;
            } else if (node == head) {
                node.next.prev = null;
                head = node.next;
            } else if (node == tail) {
                node.prev.next = null;
                tail = node.prev;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            totalSpending -= expense.getAmount();
            size--;
            double updateCategorySpending = 
                categorySpending.get(expense.getCategory())
                - expense.getAmount();
            categorySpending.replace(expense.getCategory(), updateCategorySpending);
            return true;
        }
    }

    // Return an array representation of the list
    public Expense[] toArray() {
        ExpenseNode current = head;
        Expense[] arr = new Expense[this.size()];
        for (int index = 0; index < this.size(); index++) {
            arr[index] = current.expense;
            current = current.next;
        }
        return arr;
    }

    // Return boolean for if this ExpenseList equals the other ExpenseList
    public boolean equals (ExpenseList other) {
        if (this.size != other.size()
        || totalSpending != other.getTotalSpending()) {
            return false;
        }
        Iterator<Expense> itr = other.iterator();
        for (Expense expense : this) {
            if ( !expense.equals(itr.next()) ) {
                return false;
            }
        }
        for (String key : categorySpending.keySet()) {
            if (this.getCategorySpending(key) != other.getCategorySpending(key)) {
                return false;
            }
        }
        return true;
    }

    public ExpenseList copy() {
        ExpenseList copy = new ExpenseList();
        // this ExpenseList is empty
        if (this.isEmpty()) {
            return copy;
        }
        copy.totalSpending = this.totalSpending;
        copy.size = this.size;
        for (String key : this.categorySpending.keySet()) {
            copy.categorySpending.put(key, this.getCategorySpending(key));
        }

        copy.head = new ExpenseNode(this.head.expense.copy(), null, null);
        // this ExpenseList has ONE node
        if (this.head.next == null) {
            copy.tail = copy.head;
            return copy;
        }

        
        ExpenseNode thisCurrent = this.head;
        ExpenseNode copyCurrent = copy.head;
        
        while (thisCurrent.next != null) {
            copyCurrent.next = new ExpenseNode(thisCurrent.next.expense.copy(), null, copyCurrent);
            thisCurrent = thisCurrent.next;
            copyCurrent = copyCurrent.next;
        }
        copy.tail = copyCurrent;
        return copy;
    }

    public void confirmSave (boolean confirmed, String filename) { 
        // no copy was made so no changes were made. No changes to save, so don't save, and return instead
        if (copy == null) {
            return;
        // Changes were made, and save is confirmed (confirm is true)
        } else if ( confirmed ) { 
            saveToCSV(filename); 
        // Changes were made, but save is cancelled (confirm is false)
        } else { 
            this.head = copy.head; 
            this.tail = copy.tail; 
            this.totalSpending = copy.totalSpending; 
            this.size = copy.size; 
            this.categorySpending = copy.categorySpending; 
        } 
        this.copy = null; 
     }

    // write to csv, return true if successful, each line is an expense, each comma is a field
    private boolean saveToCSV(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("Name,Category,Date,Amount\n");
            ExpenseNode current = head;
            while (current != null) {
                String name = current.expense.getName();
                String category = current.expense.getCategory();
                String date = current.expense.getLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                String amount = String.format("%.2f", current.expense.getAmount());
                writer.write(name + "," + category + "," + date + "," + amount);
                
                writer.write("\n");
                current = current.next;
            }
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    // load from file in src/main/resources/com/application/budgeter/expensedata , return true if successful
    // each line is an expense, each comma is a field (name, category, date, amount)
    public boolean loadFromCSV(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            // print reader added
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(","); // create array of fields split by comma
                // add fields to expense
                String name = fields[0]; 
                String category = fields[1];
                LocalDate date = LocalDate.parse(fields[2], DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                double amount = Double.parseDouble(fields[3]);
                // create expense and add to list
                Expense expense = new Expense(name, category, date, amount);
                this.add(expense); 
                line = reader.readLine();
            }
            reader.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // return Iterator instance
    public Iterator<Expense> iterator() {
        return new ExpenseListIterator();
    }

    private class ExpenseListIterator implements Iterator<Expense> {
        ExpenseNode current = head;
          
        @Override
        public boolean hasNext()  {
            return current != null;
        }
          
        @Override
        public Expense next()
        {
            Expense expense = current.expense;
            current = current.next;
            return expense;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(); 
        }  
    }

    // return backwards Iterator instance
    public Iterator<Expense> descendingIterator() {
        return new DescendingExpenseListIterator();
    }

    private class DescendingExpenseListIterator implements Iterator<Expense> {
        ExpenseNode current = tail;
          
        @Override
        public boolean hasNext()  {
            return current != null;
        }
          
        @Override
        public Expense next()
        {
            Expense expense = current.expense;
            current = current.prev;
            return expense;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(); 
        }  
    }
}  // End of ExpenseList class