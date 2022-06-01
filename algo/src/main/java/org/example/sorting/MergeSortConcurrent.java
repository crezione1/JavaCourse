package org.example.sorting;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MergeSortConcurrent extends RecursiveTask<int[]> {

    public static void main(String[] args) {

        int[] unsortedArray = {10, 9, 8, 7, 6, 5, -100, 4, 3, 2, 1, 0};
        int[] sortedArray = ForkJoinPool.commonPool().invoke(new MergeSortConcurrent(unsortedArray));
        System.out.println(Arrays.toString(sortedArray));
    }

    private int[] array;

    public MergeSortConcurrent(int[] input) {
        array = input;
    }

    @Override
    protected int[] compute() {
        if (array.length < 2) {
            return array;
        }

        var left = Arrays.copyOfRange(array, 0, array.length / 2);
        var right = Arrays.copyOfRange(array, array.length / 2, array.length);

        var leftTask = new MergeSortConcurrent(left);
        var rightTask = new MergeSortConcurrent(right);

        return merge(array, leftTask.fork().join(), rightTask.fork().join());
    }

    private int[] merge(int[] array, int[] left, int[] right) {
        int leftIndex = 0, rightIndex = 0, totalIndex = 0;

        while (leftIndex < left.length && rightIndex < right.length) {
            if (left[leftIndex] >= right[rightIndex]) {
                array[totalIndex++] = right[rightIndex++];
            } else {
                array[totalIndex++] = left[leftIndex++];
            }
        }

        while (leftIndex < left.length) {
            array[totalIndex++] = left[leftIndex++];
        }

        while (rightIndex < right.length) {
            array[totalIndex++] = right[rightIndex++];
        }

        return array;
    }


}
