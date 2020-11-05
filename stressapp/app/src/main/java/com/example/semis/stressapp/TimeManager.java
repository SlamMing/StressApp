package com.example.semis.stressapp;

 public class TimeManager {
    private long lastTime;
    private long lastTimeWrote;
    private final long dt = 200;
    private boolean lowBattery = false;
     private final long dtLow = 500;
    private final long featureRate = 25000;
    private static final TimeManager CurrInstance = new TimeManager();

     public static TimeManager getInstance() {
         return CurrInstance;
     }
    private TimeManager(){
        lastTime = System.currentTimeMillis();
        lastTimeWrote = System.currentTimeMillis();
    }
    public void updateTime(){
        lastTime = System.currentTimeMillis();
        lastTimeWrote = System.currentTimeMillis();
    }
     boolean isTimeToCollect() {
         if (lowBattery) {
             if (System.currentTimeMillis() - lastTime > dtLow) {
                 lastTime = System.currentTimeMillis();
                 return true;
             }else{
                 return false;
             }
         } else if (System.currentTimeMillis() - lastTime > dt) {
             lastTime = System.currentTimeMillis();
             return true;
         } else {
             return false;
         }
     }

     public void setLowBattery(boolean lowBattery) {
         this.lowBattery = lowBattery;
     }

     boolean isTimeToWrite(){
        if(System.currentTimeMillis() - lastTimeWrote > featureRate){
            lastTimeWrote = System.currentTimeMillis();
            return true;
        }else return false;
    }
}
