package com.treeApplet;



import processing.core.PApplet;
import processing.core.PVector;

public class Ramo {
    float demultiplier = 0.84f;
    PVector pos, vel;
    float r = 70, a, decay = 0.994f;
    int life = 0;

    private PApplet sketch;
    Ramo(float x, float y, int best, PApplet sketch){
        pos = new PVector(x,y);
        r = r/(best+1) + 10;  //refactoring

        a = PApplet.PI;
        vel = new PVector(PApplet.sin(a)*r/13, PApplet.cos(a)*r/13);
        this.sketch = sketch;
    }
    Ramo(Ramo father, float angle){
        pos = new PVector(father.pos.x, father.pos.y);
        r = father.r*demultiplier;
        sketch = father.sketch;
        life = father.life;
        a = angle;
        vel = new PVector(PApplet.sin(a)*r/9, PApplet.cos(a)*r/9);

    }

    void update(){
        r*=decay;
        pos.add(vel);
        life ++;
    }

    void show(){
        sketch.fill(127, 46, 24);
        sketch.ellipse(pos.x, pos.y, r, r);
    }
}
