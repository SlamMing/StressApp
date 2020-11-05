package com.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semis.stressapp.Centroid;
import com.example.semis.stressapp.FeatureData;
import com.example.semis.stressapp.IOmanager;
import com.example.semis.stressapp.R;
import com.example.semis.stressapp.Viewer;
import com.treeApplet.Sketch;

import processing.android.PFragment;

import static com.example.semis.stressapp.MainActivity.last;

public class NavStress extends Fragment {
    IOmanager iom;
    View current;
    Viewer viewer;
    Centroid[] centroids;
    Sketch sketch;
    TextView tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        current = inflater.inflate(R.layout.frag_stressapp, container, false);

        iom = IOmanager.getInstance();
         viewer = new Viewer(getActivity());
         centroids = iom.getCentroids();
        tv = current.findViewById(R.id.stressText);
        if(!isClassifierReady()){
            Toast.makeText(getActivity(), "classifier need more data", Toast.LENGTH_SHORT).show();

            tv.setText("classifier need more data");
            tv.setVisibility(View.VISIBLE);
        }else if(last==null){
            Toast.makeText(getActivity(), "not enough data", Toast.LENGTH_SHORT).show();

            tv.setText("classifier need more time");
            tv.setVisibility(View.VISIBLE);
        }else{calculateStress();}

        return current;
    }
    boolean isClassifierReady(){
        Centroid[] centroids = iom.getCentroids();
        boolean val = true;
        for(Centroid c : centroids){
            if(c.sampleSize < home.treshold)
                val = false;
        }

        return val;
    }
    void resetFrame() {
        FrameLayout f = current.findViewById(R.id.tree);
        f.removeAllViews();

    }

    void drawTree(int bestResult, double confidence) {
        resetFrame();
        sketch = new Sketch(bestResult, confidence, getActivity());
        PFragment fragment = new PFragment(sketch);
        fragment.setView(current.findViewById(R.id.tree), getActivity());
        FrameLayout f = current.findViewById(R.id.tree);
        f.setVisibility(View.VISIBLE);
    }
    double getConfidence(double[] d, int best) {
        return (d[0] + d[1] + d[2]) / d[best];


    }
    void calculateStress(){
        double min = 99999999;
        int bestResult = 3;
        FeatureData weight = iom.getWeights();
        double[] distances = new double[3];
        centroids = iom.getCentroids();
        FeatureData[] fd = new FeatureData[3];
        fd[0] = centroids[0].getNormalized();
        fd[1] = centroids[1].getNormalized();
        fd[2] = centroids[2].getNormalized();
        for (int i = 0; i < centroids.length; i++) {
            double temp = fd[i].euclideanDistance(last.getNormalized(), weight);
            distances[i] = temp;
            Log.wtf("distance", Double.toString(temp));
            if (temp < min) {
                min = temp;
                bestResult = i;
            }
        }
        tv.setVisibility(View.VISIBLE);
        double confidence = getConfidence(distances, bestResult);

        drawTree(bestResult, confidence);
        viewer.showStressText(bestResult, confidence, tv);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(
                    requestCode, permissions, grantResults);
        }
    }

    public void onNewIntent(Intent intent) {
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }

}
