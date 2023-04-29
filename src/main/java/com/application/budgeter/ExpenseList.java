package com.application.budgeter;

import java.time.*;

public class ExpenseList {
    private ExpenseNode head;
    private ExpenseNode tail;

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


    
    // add to end of list. Increment ID from current tail node's expense ID
    public void add ( LocalDate localDate, String name, String category, int amount  ) {
        int ID;
        if ( this.isEmpty() ) {
            ID = 1;
        } else {
            ID = tail.expense.getID() + 1;
        }        
        Expense expense = new Expense( ID, name, localDate, category, amount);
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
        ExpenseNode node = this.getNode(target);
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public boolean isEmpty() {
        return head == null;
    }

    // implement iterator? https://www.geeksforgeeks.org/java-implementing-iterator-and-iterable-interface/
    
}
