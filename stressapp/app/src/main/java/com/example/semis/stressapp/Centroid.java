package com.example.semis.stressapp;

import java.util.ArrayList;

 public class Centroid extends FeatureData {
     public long sampleSize;
    Centroid(String csv, long n){
        super(csv);
        sampleSize = n;
    }


    String toCSVString(){
        return super.toCSVString()+ System.getProperty("line.separator") + Long.toString(sampleSize) + System.getProperty("line.separator");
    }
    void updateData(ArrayList<FeatureData> newData){
        FeatureData sum = new FeatureData(0);
        for(FeatureData fd : newData){
            sum.addition(fd);
        }
        mult(sampleSize);
        addition(sum);
        divideBy(sampleSize + newData.size());
        sampleSize+= newData.size();
    }
}
