package com.robert.microservice.sort;

import java.util.Arrays;

public class MergeSort {
    /**
     * 归并排序
     *
     * @param array
     * @return
     */
    public static long[] MergeSort(long[] array) {
        if (array.length < 2) return array;
        int mid = array.length / 2;
        long[] left = Arrays.copyOfRange(array, 0, mid);
        long[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(MergeSort(left), MergeSort(right));
    }
    /**
     * 归并排序——将两段排序好的数组结合成一个排序数组
     *
     * @param left
     * @param right
     * @return
     */
    public static long[] merge(long[] left, long[] right) {
        long[] result = new long[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length)
                result[index] = right[j++];
            else if (j >= right.length)
                result[index] = left[i++];
            else if (left[i] > right[j])
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }
}
