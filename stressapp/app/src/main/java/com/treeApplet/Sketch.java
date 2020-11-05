package com.treeApplet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

import com.example.semis.stressapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Sketch extends PApplet {
    ArrayList<Ramo> albero = new ArrayList<>();
    float red; //colore foglie
    int lifes = 1000, beste, max, min, spawn = 20; //max iterations
    Activity current;
    public Sketch(int best, double confidence, Activity current){
        this.current = current;
        beste = best;
        red = map((float)confidence, 20, 2,  0, 255);
        max = 25 + best*15;
        min = 15 + best*5;
    }
    public void settings() {
        size(width, height);
    }

    public void setup() {
        background(194,231,242);
        albero.add(new Ramo(500, 1050, beste,  this));

        ellipseMode(CENTER);
        noStroke();
    }

    public void draw() {
        for(int i = 0; i < albero.size(); i++){
            Ramo r;
            if((r =albero.get(i))==null) break;

            if(r.r < 2.5){
                albero.remove(i);
                drawLeaf(r);
                if(i!=0) i--;
                continue;
            }
            if(r.life > spawn){
                float f = map(r.life,0, 250, max, min);
                if(random(f)<1){

                        albero.add(new Ramo(r, r.a + radians(random(5,35))));
                        if(random(100) < 1) {
                            albero.add(new Ramo(r, r.a + radians(random(5, 35))));

                        }
                        r.r *= r.demultiplier;
                        r.a -= radians(random(5,35));
                        r.vel = new PVector(sin(r.a)*r.r/9, cos(r.a)*r.r/9);
                }
            }
        }
        for(Ramo ramo : albero){
            ramo.update();
            ramo.show();
        }
        if(albero.size()==0){
            saveFrame(Environment.getExternalStorageDirectory().toString() + "/datax/last.jpg");
        }
    }


    void drawLeaf(Ramo p){
        fill(random(red-20, red + 20), random(200, 240),random(20, 40), 125);
        pushMatrix();
        translate(p.pos.x,p.pos.y);
        rotate(p.a);
        ellipse(0,0,random(7,10),random(10,30));
        popMatrix();
    }
    public void takeScreenshot() {


        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/datax/last.jpg";

            // create bitmap screen capture
            FrameLayout f = current.findViewById(R.id.tree);
            View v1 = f;
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

}
