package com.cmput301A1.heartmonitor;

import java.util.Date;

public class DataEntry {

    private Date date;
    private int systolic;
    private int diastolic;
    private int heartRate;
    private String comment;
    private boolean abnormal;

    public DataEntry(Date date, int systolic, int diastolic, int heartRate, String comment) {
        this.date = date;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.heartRate = heartRate;
        this.comment = comment;

        // Check if the systolic / diastolic readings are abnormal
        this.abnormal = (systolic < 90 || systolic > 140 || diastolic < 60 || diastolic > 90);
    }

    public boolean isAbnormal() {
        return abnormal;
    }

    public Date getDate() {
        return date;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
