package com.example.semis.stressapp;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import processing.core.PApplet;

public class IOmanager {
    private File myFile;
    private String filenameL, filenameN, filenameH, filenameCEN, filenameW;
    private ArrayList<AccVector> chunk;
    private FeatureBlock fb;
    private CircularArray ca;
    private ArrayList<FeatureData> fds;
    private static final IOmanager ourInstance = new IOmanager();

     public static IOmanager getInstance() {
         return ourInstance;
     }



   private IOmanager(){

        File folder = new File(Environment.getExternalStorageDirectory().toString() +"/datax");
        boolean res = false;
        if(!folder.exists())
            res = folder.mkdir();
        ca = new CircularArray(20);
        fb = new FeatureBlock();
        filenameL = folder.toString() + "/" + "low.csv";
       filenameN = folder.toString() + "/" + "normal.csv";
       filenameH= folder.toString() + "/" + "high.csv";
       filenameCEN = folder.toString() + "/" + "centroids.csv";
       filenameW = folder.toString() + "/" + "weights.csv";
        reset();
        fds = new ArrayList<>();
    }


     void init(){
        try {
            myFile = new File(filenameL);
            if(!myFile.exists())
                myFile.createNewFile();
            myFile = new File(filenameN);
            if(!myFile.exists())
                myFile.createNewFile();
            myFile = new File(filenameH);
            if(!myFile.exists())
                myFile.createNewFile();
            myFile = new File(filenameW);
            if(!myFile.exists()) {
                myFile.createNewFile();
                initWeights();
            }
            myFile = new File(filenameCEN);

            if(!myFile.exists())
                myFile.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

     void writeData(){
  // aggiungo la sessione di dati ad un feature block
            fb.addData(chunk);
//moltiplico per i weight

  //aggiungo all'array circolare
            ca.add(new FeatureData(fb.toCSVString()));

        MainActivity.setLast(fb.toCSVString());
        MainActivity.ready = true;
        reset();
    }

     void addData(double x, double y, double z){
        chunk.add(new AccVector(x, y, z));
    }

    private void reset(){
        chunk = new ArrayList<>();
        fb.reset();
    }

    void updateData(String stressLevel) {
        //scrivi nel file *stressLevel*.csv i dati raccolti nella sessione current
        String filename = Environment.getExternalStorageDirectory().toString() + "/datax/" + stressLevel + ".csv";
        File f = new File(filename);
        fds = ca.getList();
        try {
            FileOutputStream fOut = new FileOutputStream(f, true);
            OutputStreamWriter writer = new OutputStreamWriter(fOut);

                for (FeatureData fd : fds) {
                    writer.append(fd.toCSVString());
                    writer.append(System.getProperty("line.separator"));
                }



            writer.close();
            fOut.close();



        } catch (IOException e) {
            e.printStackTrace();
        }
        //pulisci array
        ca.clear();


        //comunica che non è pronto a scrivere

        MainActivity.ready = false;

        //update centroidi
        updateCentroids(stressLevel);


        //ricalcolo le weights

        updateWeights(getCentroids());

    }

    void updateWeights(Centroid[] c){
        FeatureData weight = new FeatureData(0);
        double[] perc = new double[21];
        double[] perc1 = new double[21];
        double[] perc2 = new double[21];
        FeatureData[] fd = new FeatureData[3];
        fd[0] =  c[0].getNormalized();
        fd[1] =  c[1].getNormalized();
        fd[2] =  c[2].getNormalized();
        for(int i = 0; i < c[0].features.length; i++){
            perc[i] = PApplet.map((float)(fd[0].features[i]-fd[1].features[i]), -2, 2, 0, 1);
        }
        for(int i = 0; i < c[0].features.length; i++){
            perc1[i] = PApplet.map((float)(fd[0].features[i]-fd[2].features[i]), -2, 2, 0, 1);
        }
        for(int i = 0; i < c[0].features.length; i++){
            perc2[i] = PApplet.map((float)(fd[1].features[i]-fd[2].features[i]), -2, 2, 0, 1);
        }
        for(int i = 0; i < weight.features.length; i++){
            weight.features[i] += perc[i] + perc1[i] + perc2[i];
        }
        File f = new File(filenameW);


        try {
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter writer = new OutputStreamWriter(fOut);
            writer.write(weight.toCSVString());
            writer.close();
            fOut.close();
        }catch(IOException e){ e.printStackTrace();}
    }

    public Centroid[] getCentroids(){
         Centroid[] temp = new Centroid[3];
         try {
             File f = new File(filenameCEN);

             //se il file è vuoto creo i centroidi in base ai dati attuali
             if(f.length()==0){
                 writeCentroids();
             }
             FileReader fr = new FileReader(f);
             BufferedReader br = new BufferedReader(fr);
             String line, line2;
             for (int i = 0; i < temp.length; i++) {
                 line = br.readLine();
                 line2 = br.readLine();
                 temp[i] = new Centroid(line, Long.parseLong(line2));
             }
         }catch(IOException e) { e.printStackTrace(); }
        return temp;
    }


  void updateCentroids(String stressLevel){
            long size = 0;
            Centroid[] centroids =  getCentroids();

            //aggiorno i centroidi
            switch(stressLevel){
                case("low"): centroids[0].updateData(fds);
                    break;
                case("normal"): centroids[1].updateData(fds);
                    break;
                case("high"): centroids[2].updateData(fds);
                    break;
                default:
                    break;
            }
            fds = new ArrayList<>();

            //riscrivo il file dei centroidi
            try {
                FileOutputStream fOut = new FileOutputStream(new File(filenameCEN));
                OutputStreamWriter writer = new OutputStreamWriter(fOut);
                writer.write(centroids[0].toCSVString() + centroids[1].toCSVString() + centroids[2].toCSVString());

            }catch(IOException e) { e.printStackTrace(); }
            }
   void writeCentroids(){

       try {
           FileOutputStream fOut = new FileOutputStream(new File(filenameCEN), true);
           OutputStreamWriter writer = new OutputStreamWriter(fOut);

           //leggo i valori per stress low
           FileReader fis = new FileReader(new File(filenameL));
           BufferedReader br = new BufferedReader(fis);
           FeatureData temp = new FeatureData(0);
           String line;
           long size = 0;
           while ((line = br.readLine()) != null) {
               temp.addition(new FeatureData(line));
               size ++;
           }
            temp.divideBy(size);
           writer.append(temp.toCSVString() + System.getProperty("line.separator") + Long.toString(size) + System.getProperty("line.separator"));
           br.close();
           fis.close();

           //leggo i valori per stress normal

            fis = new FileReader(new File(filenameN));
            br = new BufferedReader(fis);
            temp = new FeatureData(0);
            size = 0;
           while ((line = br.readLine()) != null) {
               temp.addition(new FeatureData(line));
               size ++;
           }
           temp.divideBy(size);
           writer.append(temp.toCSVString()+ System.getProperty("line.separator") + Long.toString(size) + System.getProperty("line.separator"));
           br.close();
           fis.close();

           //leggo i valori per stress high
           fis = new FileReader(new File(filenameH));
           br = new BufferedReader(fis);
           temp = new FeatureData(0);
           size = 0;
           while ((line = br.readLine()) != null) {
               temp.addition(new FeatureData(line));
               size ++;
           }
           temp.divideBy(size);
           writer.append(temp.toCSVString() + System.getProperty("line.separator") + Long.toString(size) + System.getProperty("line.separator"));
           br.close();
           fis.close();
           writer.close();
           fOut.close();




       } catch (IOException e) {
           e.printStackTrace();
       }

   }
    public FeatureData getWeights(){
         FeatureData temp = new FeatureData(0);
         try {
             FileReader fis = new FileReader(new File(filenameW));
             BufferedReader br = new BufferedReader(fis);
             temp = new FeatureData(br.readLine());
             br.close();
            fis.close();
         }catch(IOException e){ e.printStackTrace();}
         return temp;
   }
   void initWeights() {
       //weights.csv
       File f = new File(filenameW);
       FeatureData ones = new FeatureData(1);

       try {
           FileOutputStream fOut = new FileOutputStream(f);
           OutputStreamWriter writer = new OutputStreamWriter(fOut);
            writer.write(ones.toCSVString());
            writer.close();
            fOut.close();
       }catch(IOException e){ e.printStackTrace();}
   }



}
