package org.richcodes.config;

import org.richcodes.model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class BatchProcessing {
    private int size = 5;
    private List<Tweet> tweets;

    public BatchProcessing(int size) {
        if(size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        if(size > 5){
            this.size = size;
        }

    }

    public <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        if (list.isEmpty()) {
            return partitions;
        }
        int length = list.size();
        int numOfPartitions = length / size + ((length % size == 0) ? 0 : 1);
        for (int i = 0; i < numOfPartitions; i++) {
            int from = i * size;
            int to = Math.min((i * size + size), length);
            partitions.add(list.subList(from, to));
        }
        return partitions;
    }
}
