package com.robert.microservice.sort;

import java.util.ArrayList;
import java.util.List;

public class BucketSort {

    public static List<Long> sort(List<Long> array, int bucketSize) {
        if (array == null || array.size() < 2)
            return array;
        long max = array.get(0), min = array.get(0);
        // 找到最大值最小值
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) > max)
                max = array.get(i);
            if (array.get(i) < min)
                min = array.get(i);
        }
        int bucketCount = (int)((max - min) / bucketSize + 1);
        ArrayList<ArrayList<Long>> bucketArr = new ArrayList<>(bucketCount);
        ArrayList<Long> resultArr = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            bucketArr.add(new ArrayList<Long>());
        }
        for (int i = 0; i < array.size(); i++) {
            bucketArr.get((int)((array.get(i) - min) / bucketSize)).add(array.get(i));
        }
        for (int i = 0; i < bucketCount; i++) {
            if (bucketSize == 1) {
                for (int j = 0; j < bucketArr.get(i).size(); j++)
                    resultArr.add(bucketArr.get(i).get(j));
            } else {
                if (bucketCount == 1)
                    bucketSize--;
                List<Long> temp = sort(bucketArr.get(i), bucketSize);
                for (int j = 0; j < temp.size(); j++)
                    resultArr.add(temp.get(j));
            }
        }
        return resultArr;
    }
}
