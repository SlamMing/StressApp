package com.example.semis.stressapp;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.math.BigDecimal;

public class Viewer {
    private Activity current;
    public Viewer(Activity currentActivity){
        current = currentActivity;
    }


    public void hideButton(int id){
        Button b = current.findViewById(id);
        b.setVisibility(Button.INVISIBLE);
    }
    public void showButton(int id){
        Button b = current.findViewById(id);
        b.setVisibility(Button.VISIBLE);
    }
    public void showStress(){
        RatingBar mRatingBar = current.findViewById(R.id.ratingStress);
        TextView mRatingScale = current.findViewById(R.id.strext);
        Button mSendFeedback = current.findViewById(R.id.submit);
        mRatingBar.setVisibility(RatingBar.VISIBLE);
        mRatingScale.setVisibility(TextView.VISIBLE);
        mSendFeedback.setVisibility(Button.VISIBLE);
    }
    public void hideStress(){
        RatingBar mRatingBar = current.findViewById(R.id.ratingStress);
        TextView mRatingScale = current.findViewById(R.id.strext);
        Button mSendFeedback = current.findViewById(R.id.submit);
        mRatingBar.setVisibility(RatingBar.INVISIBLE);
        mRatingScale.setVisibility(TextView.INVISIBLE);
        mSendFeedback.setVisibility(Button.INVISIBLE);
    }


    public void setText(String s, int id){
        TextView tv = current.findViewById(id);
        tv.setText(s);
    }
    public void showText(int id, Activity current){
        TextView tv = current.findViewById(id);
        tv.setVisibility(TextView.VISIBLE);
    }

    void hideText(int id){
        TextView tv = current.findViewById(id);
        tv.setVisibility(TextView.INVISIBLE);
    }
    public void showFrame(){
        FrameLayout frame = current.findViewById(R.id.tree);
        frame.setVisibility(TextView.VISIBLE);
    }

    void hideFrame(){
        FrameLayout frame = current.findViewById(R.id.tree);
        frame.setVisibility(TextView.INVISIBLE);
    }
    public void showStressText(int bestResult, double confidence, TextView s){
        switch(bestResult){
            case(0): s.setText("Estimated stress: " +
                    "LOW" + System.getProperty("line.separator") +
                    "Confidence: " + new BigDecimal(confidence).setScale(3, BigDecimal.ROUND_DOWN).toString());
                break;
            case(1): s.setText("Estimated stress: " +
                    "NORMAL"+ System.getProperty("line.separator") +
                    "Confidence: " + new BigDecimal(confidence).setScale(3, BigDecimal.ROUND_DOWN).toString());
                break;
            case(2): s.setText("Estimated stress: " +
                    "HIGH" + System.getProperty("line.separator") +
                    "Confidence: " + new BigDecimal(confidence).setScale(3, BigDecimal.ROUND_DOWN).toString());
                break;
            case(3):
                Log.wtf("errore centroidi", "overflow best result");
                break;
            default:
                break;
        }
    }
}

