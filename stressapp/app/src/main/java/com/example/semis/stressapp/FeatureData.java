package com.example.semis.stressapp;

import java.math.BigDecimal;

import processing.core.PApplet;

public class FeatureData {
    double[] features;

    //inizializzazione da stringa csv

    FeatureData(String csv){
        features = new double[21];
        String[] tokens = csv.split(", ");
        int i = 0;
        for(String s : tokens){
            features[i] = Double.parseDouble(s);
            i++;
        }
    }

    //inizializzazione a zero

    FeatureData(double startingValue){
        features = new double[21];
        for(int i = 0; i < features.length; i++){
            features[i] = startingValue;
        }
    }
   public double euclideanDistance(FeatureData d, FeatureData weight){
        double dist = 0;
        for(int i = 0; i < features.length; i++){
            dist+= Math.pow(features[i] - d.features[i], 2)*weight.features[i];
        }
        return Math.sqrt(dist);
   }
    void addition(FeatureData d){
        for(int i = 0; i < features.length; i++){
            features[i] += d.features[i];
        }
    }
    void divideBy(long divider){
        if(divider == 0)
            divider++;
        for(int i = 0; i < features.length; i++){
            features[i] = features[i]/divider;
        }
    }
    void divideBy(FeatureData divider){

            for (int i = 0; i < features.length; i++) {
                if(divider.features[i] == 0){
                    continue;
                }
                features[i] = features[i] / divider.features[i];
            }

    }
    void mult(long multiplier){
        for(int i = 0; i < features.length; i++){
            features[i] = features[i]*multiplier;
        }
    }
    void mult(FeatureData multiplier){
        for(int i = 0; i < features.length; i++){
            features[i] = features[i]*multiplier.features[i];
        }
    }
    String toCSVString(){
        String temp = "";
        for(double d : features){
            temp = temp.concat(new BigDecimal(d).setScale(7, BigDecimal.ROUND_DOWN).toString() + ", ");
        }
        return temp;
    }

    public FeatureData getNormalized(){
        FeatureData fd = new FeatureData(0);
        for(int i = 0; i < 3; i++){
            fd.features[i*7] = PApplet.map((float)features[i*7], -13, 13, -1, 1);
            fd.features[i*7 + 1] = PApplet.map((float)features[i*7 + 1], 0, 3, -1, 1);
            fd.features[i*7 + 2] = PApplet.map((float)features[i*7 + 2], 0, 3, -1, 1);
            fd.features[i*7 + 3] = PApplet.map((float)features[i*7 + 3], -13, 13, -1, 1);
            fd.features[i*7 + 4] = PApplet.map((float)features[i*7 + 4], -13, 13, -1, 1);
            fd.features[i*7 + 5] = PApplet.map((float)features[i*7 + 5], -10, 10, -1, 1);
            fd.features[i*7 + 6] = PApplet.map((float)features[i*7 + 6], 0, 13, -1, 1);
        }
        return fd;
    }
}
