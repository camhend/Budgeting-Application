package com.application.budgeter;

import java.util.Iterator;
import java.time.*;
import java.util.NoSuchElementException;

// TODO: implement an Edit feature

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


    // Add new Expense to end of list. Takes expense details as parameters
        public void add ( String name, String category, LocalDate localDate, int amount) { 
        Expense expense = new Expense( name, category, localDate, amount);
        ExpenseNode node = new ExpenseNode (expense, null, null);
        if ( this.isEmpty() ) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        totalSpending += amount;
        size++;
    }

    // Add Expense to end of list. Takes Expense object as parameter 
    public void add ( Expense expense) { 
        ExpenseNode node = new ExpenseNode (expense, null, null);
        if ( this.isEmpty() ) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        totalSpending += expense.getAmount();
        size++;
    }

    // Get the ExpenseNode that contains the given Expense
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

    // Return whether list contains the given Expense
    public boolean contains( Expense expense ) {
        ExpenseNode current = head;
        while (current != null) {
            if (current.expense.equals( expense )) {
                return true;
            } else {
                current = current.next;
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
    public void remove( Expense expense ) {
        if (head == null) {
            throw new NoSuchElementException("Cannot remove an Expense not in the list.");
        } else {
            ExpenseNode node = this.getNode(expense);
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

    // return Iterator instance
    public Iterator iterator() {
        return new ExpenseListIterator();
    }

    private class ExpenseListIterator implements Iterator<Expense> {
        ExpenseNode current = tail;
          
        public boolean hasNext()  {
            return current != null;
        }
          
        // TODO: FIX
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


