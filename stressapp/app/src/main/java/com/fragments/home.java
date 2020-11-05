package com.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.semis.stressapp.Centroid;
import com.example.semis.stressapp.IOmanager;
import com.example.semis.stressapp.R;

public class home extends Fragment {
   public static final long treshold = 40;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        IOmanager iom = IOmanager.getInstance();

        /*ImageView lastTree = view.findViewById(R.id.lastTree);
            File img = new File(Environment.getExternalStorageDirectory().toString() + "/datax/last.png");
            if(img.exists()) {
                Bitmap lastAlbero = BitmapFactory.decodeFile(img.getAbsolutePath());
                lastTree.setImageBitmap(lastAlbero);
            }*/
            Centroid[] centroids = iom.getCentroids();
        if(centroids[0].sampleSize > treshold){
            ImageView t = view.findViewById(R.id.low);
            t.setImageResource(R.drawable.green);
        }
            if(centroids[1].sampleSize > treshold){
                ImageView t = view.findViewById(R.id.normal);
                t.setImageResource(R.drawable.green);
            }
                if(centroids[2].sampleSize > treshold){
                    ImageView t = view.findViewById(R.id.high);
                    t.setImageResource(R.drawable.green);
                }


                return view;
    }

}
