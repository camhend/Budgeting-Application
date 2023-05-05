package com.application.budgeter;

import java.util.Iterator;
import java.time.*;

// TODO: make CSV reader / writer
    // consider: how many expense to load? 
    // how far back to go?
    // How to add newly added expenses to CSV log? 
    // What if a really old date is added? How will the newly added date be added to the CSV in a sorted manner?

// This class defines a LinkedList used for storing
// Expense objects. Oldest items are at the head, newest at the tail.
public class ExpenseList implements Iterable<Expense> {
    private ExpenseNode head;
    private ExpenseNode tail;
    private int totalSpending;
    private int size;

    // ExpenseList constructor
    public ExpenseList () {
        this.head  = null;
        this.tail = null;
        this.size = 0;
        this.totalSpending = 0;
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
    
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }
    
    public int getTotalSpending() {
        return totalSpending;
    }

    // Add new Expense to the list in sorted order by date
    // Takes Expense fields as parameters
        public void add ( String name, String category, LocalDate localDate, int amount) { 
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
    }

    // Add new Expense to list in sorted order by date
    // Takes Expense object as parameter
    public void add ( Expense newExpense) { 
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
        ExpenseNode node = this.getNode(old);
        if (node == null) { // expense not found in list
            return false; 
        }
        node.expense = updated;
        // If expense date was changed, then move the node
        // to the correct sorted position.
        if ( !updated.getLocalDate().equals(old.getLocalDate()) ) {
            // First, remove edited node from the current position
            if (node == head) {
                node.next.prev = null;
                head = node.next;
            } else if (node == tail) {
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
                    node.next = head;
                    node.prev = null;
                    head.prev = node;
                    head = node;
                // else traverse for the correct position
                } else {
                    ExpenseNode current = node;
                    // traverse until the current node date is before the updated date 
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

    // clear the list of all elements
    public void clear () {
        this.head = null;
        this.tail = null;
        size = 0;
        totalSpending = 0;
    }

    // Remove the ExpenseNode that contains the given Expense
    // Return true if successfully removed.
    public boolean remove( Expense expense ) {
        ExpenseNode node = this.getNode(expense);
        if (node == null) {
            return false;
        } else {
            if (node == head) {
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
    


} 


