package com.robert.microservice.sort;

import javax.naming.SizeLimitExceededException;
import java.util.Arrays;

public class CountSort {
    public static Long[] sort(Long[] array) throws SizeLimitExceededException{
        if (array.length == 0) return array;
        long bias, min = array[0], max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max)
                max = array[i];
            if (array[i] < min)
                min = array[i];
        }
        bias = 0 - min;
        long size = max - min + 1;
        if(size > Integer.MAX_VALUE){
            throw new SizeLimitExceededException("" + size);
        }
        long[] bucket = new long[(int)size];
        Arrays.fill(bucket, 0);
        for (int i = 0; i < array.length; i++) {
            bucket[(int)(array[i] + bias)]++;
        }
        int index = 0, i = 0;
        while (index < array.length) {
            if (bucket[i] != 0) {
                array[index] = i - bias;
                bucket[i]--;
                index++;
            } else
                i++;
        }
        return array;
    }
}
