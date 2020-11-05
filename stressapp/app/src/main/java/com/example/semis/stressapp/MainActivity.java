package com.example.semis.stressapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.fragments.AddStress;
import com.fragments.NavStress;
import com.fragments.Settings;
import com.fragments.home;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    Intent it;
    public static boolean running = false;
    private IOmanager iom;
    private Viewer viewer;
    public static FeatureData last;
    static Centroid[] centroids;

    static boolean ready = false;
    private DrawerLayout current;
    private ActionBarDrawerToggle toggle;
    private NavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }


    @Override
    protected void onStart() {
        super.onStart();
        nav = findViewById(R.id.menuNav);
        nav.setNavigationItemSelectedListener(this);

    }

    private void init() {
        isStoragePermissionGranted();
        isStorageReadPermissionGranted();
        it = new Intent(this, DataGatherer.class);
        viewer = new Viewer(this);
        iom = IOmanager.getInstance();
        iom.init();
        centroids = iom.getCentroids();
        startService(it);
        running = true;

        current = findViewById(R.id.main);
        toggle = new ActionBarDrawerToggle(this, current, R.string.open, R.string.close);
        current.addDrawerListener(toggle);
        toggle.syncState();
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new home()).commit();

    }


    //funzioni bottoni @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



    //submit
    public void addStress(View view) {
        if (!ready) {
            Toast.makeText(this, "not enough data", Toast.LENGTH_SHORT).show();
            return;
        }
        RatingBar mRatingBar = findViewById(R.id.ratingStress);

        iom.updateData(getStress(mRatingBar.getRating()));
        //leggi centroidi salvati
        centroids = iom.getCentroids();
        Toast.makeText(this, "successfully registered", Toast.LENGTH_SHORT).show();

        //vai home

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new home()).commit();

    }





    //funzioni ausiliare @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


    String getStress(float rating) {
        if (rating <= 1.5) {
            return "low";
        } else if (rating <= 3) {
            return "normal";
        } else
            return "high";
    }

    static void setLast(String csv) {
        last = new FeatureData(csv);
    }




    //controlla permessi per scrivere

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
    public boolean isStorageReadPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }






//ovverides@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    // override di alcune funzioni per evitare errori con il PApplet


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        current.closeDrawer(Gravity.START);

        int ID = menuItem.getItemId();
        switch (ID) {
            case (R.id.nav_settings):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Settings()).commit();
                break;
            case (R.id.nav_addstress):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddStress()).commit();
                break;
            case (R.id.nav_stressapp):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavStress()).commit();
                break;
            case (R.id.nav_home):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new home()).commit();
                break;
            default:
                break;
        }
        return true;
    }


}