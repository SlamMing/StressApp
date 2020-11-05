package com.example.semis.stressapp;

import java.util.ArrayList;

public class CircularArray {
    ArrayList<FeatureData> list;
    int size;
    CircularArray(int size_){
        list = new ArrayList<>();
        size = size_;
    }
    void add(FeatureData fd){
        if(list.size() >= size){
            list.remove(0);
            list.add(fd);
        } else{
            list.add(fd);
        }
    }
    ArrayList<FeatureData> getList(){
        return list;
    }
    boolean isFull(){
        return list.size() == size;
    }

    void clear(){
        list = new ArrayList<>();
    }
}
