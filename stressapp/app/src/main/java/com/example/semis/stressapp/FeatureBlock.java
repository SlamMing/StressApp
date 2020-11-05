package com.example.semis.stressapp;

import java.util.ArrayList;

 class FeatureBlock {
    private Feature fx, fy, fz;
        FeatureBlock(Feature fx_, Feature fy_, Feature fz_){
            fx = fx_;
            fy = fy_;
            fz = fz_;
        }
        FeatureBlock(){
            fx = new Feature();
            fy = new Feature();
            fz = new Feature();

        }

        String toCSVString(){
            return fx.toCSVString() + fy.toCSVString() + fz.toCSVString();
        }

        void addData(ArrayList<AccVector> ch){
            for(AccVector av : ch) {
                fx.addValue(av.getX());
                fy.addValue(av.getY());
                fz.addValue(av.getZ());
            }
        }
        void reset(){

            fx.clear();
            fy.clear();
            fz.clear();
            /*
            fx = new Feature();
            fy = new Feature();
            fz = new Feature();
*/
        }
        long length(){
            return fx.getN();
        }

}
