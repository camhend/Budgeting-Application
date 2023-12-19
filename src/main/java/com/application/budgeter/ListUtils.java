package com.application.budgeter;

import java.util.*;
/*  Authors: Sukhnain Deol, Cameron Henderson, Theodore Ingberman, and Kristopher McFarland
 *  Date: 06/2023
 *  Description: This class support additional operations on Expense Lists.
 * 
 */

public class ListUtils {
    
    public static void sort (List<Expense> list, Comparator<Expense> c) {
        Expense[] array = list.toArray(new Expense[list.size()]);
        // check for empty or small list
        mergeSort(array, c, 0, array.length - 1);
        ListIterator<Expense> itr = list.listIterator();
        for (int i=0; i < array.length; i++) {
            itr.next();
            itr.set(array[i]);
        }
    }

    // Recursively half the array into successively smaller subarrays.
    // Sort each half, then merge the two halves together.
    private static void mergeSort(Expense[] a, Comparator<Expense> c, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(a, c, left, middle);      // sort left subarray
            mergeSort(a, c, middle + 1, right); // sort right subarray
            merge(a, c, left, middle, right);   // merge the subarrays
        }
    }

    // Interal method that takes an array and indeces for the left wall,
    // middle point, and right wall. Walks through both subarrays, merging
    // elements from both arrays into one into one sorted array.
    private static void merge(Expense[] a, Comparator<Expense> c, int left, int middle, int right) {
        
        // Copy the the array into left and right subarrays
        int leftSize = middle - left + 1;
        int rightSize = right - middle;
        Expense[] leftArray = new Expense[leftSize];
        Expense[] rightArray = new Expense[rightSize];
        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = a[left + i];
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = a[middle + 1 + j];
        }

        // Walk through both arrays, compare next element
        // in both arrays, and put the smallest of the two
        // into the final array.
        int i = 0;
        int j = 0;
        int k = left;
        while (i < leftSize && j < rightSize) {
            if (c.compare(leftArray[i], rightArray[j]) <= 0) {
                a[k] = leftArray[i];
                i++;
            } else {
                a[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // If any elements left in either array, then
        // put all remaining into the final array.
        while (i < leftSize) {
             a[k] = leftArray[i];
             i++;
             k++;
        }
        while (j < rightSize) {
            a[k] = rightArray[j];
            j++;
            k++;
        }
    }

    private static void quickSort(Expense[] a, Comparator<Expense> c, int low, int high) {
        if (low >= high) {
            return;
        }
        if (low < high) {
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
