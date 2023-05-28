package com.application.budgeter;

import java.util.*;

public class ListUtils {
    
    public static void sort (List<Expense> list, Comparator<Expense> c) {
        Expense[] array = list.toArray(new Expense[list.size()]);
        // check for empty or small list
        quickSort(array, c, 0, array.length - 1);
        ListIterator<Expense> itr = list.listIterator();
        for (int i=0; i < array.length; i++) {
            itr.next();
            itr.set(array[i]);
        }
    }

    private static void quickSort(Expense[] a, Comparator<Expense> c, int low, int high) {
        if (low >= high) {
            return;
        }
        
        if (low < high) {
            //int pivot =  high; // change this (just picks the last item in the array)
            int pivot = partition(a, c, low, high);
            quickSort(a, c, low, pivot - 1);
            quickSort(a, c, pivot + 1, high);
        }
    }

    // move all items less than pivot to left, and all items greater to right of pivot
    private static int partition (Expense[] a, Comparator<Expense> c, int low, int pivot) {
        int left = low;
        int right = pivot;

        while (left < right) {
            
            while (c.compare(a[left], a[pivot]) <= 0 && left < right) {
                left++;
            }

            while (c.compare(a[right], a[pivot]) >= 0 && left < right) {
                right--;
            }

            swap(a, left, right);
        }

        swap(a, left, pivot);
        return left;

    }

    private static void swap (Expense[] a, int index1, int index2) {
        Expense temp = a[index1];
        a[index1] = a[index2];
        a[index2] = temp;
    }

}
