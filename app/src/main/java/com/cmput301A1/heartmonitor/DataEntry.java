package com.cmput301A1.heartmonitor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DataEntry {

    private DateTime date;

    private int systolic;
    private int diastolic;
    private int heartRate;
    private String comment;
    private boolean abnormal;

    public DataEntry(DateTime date, int systolic, int diastolic, int heartRate, String comment) {
        this.date = date;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.heartRate = heartRate;
        this.comment = comment;

        // Check if the systolic / diastolic readings are abnormal
        this.abnormal = (systolic < 90 || systolic > 140 || diastolic < 60 || diastolic > 90);
    }

    public DataEntry(JSONObject obj) throws JSONException {
        this.date = new DateTime();
        this.date.year = obj.getInt("year");
        this.date.month = obj.getInt("month");
        this.date.day = obj.getInt("day");
        this.date.hour = obj.getInt("hour");
        this.date.minute = obj.getInt("minute");
        this.systolic = obj.getInt("systolic");
        this.diastolic = obj.getInt("diastolic");
        this.heartRate = obj.getInt("heartRate");
        this.comment = obj.getString("comment");

        // Check if the systolic / diastolic readings are abnormal
        this.abnormal = (systolic < 90 || systolic > 140 || diastolic < 60 || diastolic > 90);
    }

    /**
     * When displayed in the list form, the date is set as the header
     * this returns the date in the form of a string.
     *
     * @return - The Date in String form
     */
    public String getHeader() {
        return String.format(Locale.CANADA, "%04d-%02d-%02d | %02d:%02d", date.year, date.month, date.day,
                date.hour, date.minute);
    }

    /**
     * When displayed in the list form, the sub data is displayed
     * as subtext under the date/time stamp
     *
     * @return - The data in subtext form
     */
    public String getSubText() {
        return "Systolic: " + systolic + " Diastolic: " + diastolic + ", Heart Rate: " + heartRate
                + '\n' + comment;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("year", getDate().year);
        obj.put("month", getDate().month);
        obj.put("day", getDate().day);
        obj.put("hour", getDate().hour);
        obj.put("minute", getDate().minute);
        obj.put("systolic", getSystolic());
        obj.put("diastolic", getDiastolic());
        obj.put("heartRate", getHeartRate());
        obj.put("comment", getComment());

        return obj;
    }

    public boolean isAbnormal() {
        return abnormal;
    }

    public DateTime getDate() {
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
