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


    // Add new Expense to the list in sorted order by date
    // Takes Expense fields as parameters
        public void add ( String name, String category, LocalDate localDate, int amount) { 
        Expense newExpense = new Expense( name, category, localDate, amount);
        ExpenseNode newNode = new ExpenseNode (newExpense, null, null);
        if ( this.isEmpty() ) {
            head = newNode;
            tail = newNode;
        } else if ( newExpense.getLocalDate().isEqual(tail.expense.getLocalDate()) ||
                    newExpense.getLocalDate().isAfter(tail.expense.getLocalDate()) ) {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        } else if ( newExpense.getLocalDate().isBefore(head.expense.getLocalDate()) ) {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
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
        if ( this.isEmpty() ) {
            head = newNode;
            tail = newNode;
        } else if ( newExpense.getLocalDate().isEqual(tail.expense.getLocalDate()) ||
                    newExpense.getLocalDate().isAfter(tail.expense.getLocalDate()) ) {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        } else if ( newExpense.getLocalDate().isBefore(head.expense.getLocalDate()) ) {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
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

    // TODO: implement edit. Return true if successfully found and edited


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
    public Iterator<Expense> iterator() {
        return new ExpenseListIterator();
    }

    private class ExpenseListIterator implements Iterator<Expense> {
        ExpenseNode current = tail;
          
        public boolean hasNext()  {
            return current != null;
        }
          
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


