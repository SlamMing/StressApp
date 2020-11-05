package com.example.semis.stressapp;

import org.apache.commons.math.stat.DescriptiveStatistics;
import org.apache.commons.math.stat.DescriptiveStatisticsImpl;

import java.math.BigDecimal;

class Feature{
   private DescriptiveStatistics ds;
   private final int cifreSignificative = 7;

    Feature(){
        ds = new DescriptiveStatisticsImpl();
    }



    void addValue(double value){
        this.ds.addValue(value);
    }

    private double getMean(){
        return this.ds.getMean();
    }

    private double getSDev(){
        return this.ds.getStandardDeviation();
    }

    private double getVar(){
        return this.ds.getVariance();
    }
    private double getMin(){
        return this.ds.getMin();
    }
    double getMax(){
        return this.ds.getMax();
    }
    private double getKrt(){
        return this.ds.getKurtosis();
    }

    long getN(){
        return this.ds.getN();
    }

    private double getRMS(){
        double temp = this.ds.getSumsq();
        temp = temp/getN();
        return Math.sqrt(temp);
    }

    void clear(){
        this.ds.clear();
    }


     String toCSVString(){
         String s = new BigDecimal(getMean()).setScale(cifreSignificative, BigDecimal.ROUND_DOWN).toString() + ", "
                 +  new BigDecimal(getSDev()).setScale(cifreSignificative, BigDecimal.ROUND_DOWN).toString() +  ", "
                 +  new BigDecimal(getVar()).setScale(cifreSignificative, BigDecimal.ROUND_DOWN).toString() +  ", "
                 +  new BigDecimal(getMin()).setScale(cifreSignificative, BigDecimal.ROUND_DOWN).toString() +  ", "
                 +  new BigDecimal(getMax()).setScale(cifreSignificative, BigDecimal.ROUND_DOWN).toString() +  ", "
                 +  new BigDecimal(getKrt()).setScale(cifreSignificative, BigDecimal.ROUND_DOWN).toString() +  ", "
                 +  new BigDecimal(getRMS()).setScale(cifreSignificative, BigDecimal.ROUND_DOWN).toString() +  ", "
                 ;
         return s;
     }
}
