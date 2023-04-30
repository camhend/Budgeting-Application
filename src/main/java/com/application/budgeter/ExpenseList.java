package com.application.budgeter;

import java.time.*;
import java.util.NoSuchElementException;

// This class defines a LinkedList used for storing
// Expense objects. Each new Expense is assigned an ID
// and added to the end of the list.
public class ExpenseList {
    private ExpenseNode head;
    private ExpenseNode tail;
    private int totalSpending;
    private int size;
    private int idCount;

    // ExpenseList constructor
    public ExpenseList () {
        this.head  = null;
        this.tail = null;
        this.size = 0;
        this.totalSpending = 0;
        this.idCount = 1;
    }

    // Nested class for LinkedList Nodes
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
    
    // Add to end of list. Increment ID from current tail node's expense ID
    public void add ( LocalDate localDate, String name, String category, int amount  ) { 
        Expense expense = new Expense( idCount, name, localDate, category, amount);
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
        idCount++;
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

    // Remove the ExpenseNode that contains the given Expense
    public void remove( Expense target ) {
        if (head == null) {
            throw new NoSuchElementException("Cannot remove an Expense not in the list.");
        } else {
            ExpenseNode node = this.getNode(target);
            node.prev.next = node.next;
            node.next.prev = node.prev;
            totalSpending -= target.getAmount();
            size--;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    // implement iterator? https://www.geeksforgeeks.org/java-implementing-iterator-and-iterable-interface/
    
}
