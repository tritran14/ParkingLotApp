package com.example.parkinglotapp.model;

import java.sql.Timestamp;
import java.util.Date;

public class TimeInOutOfFireStore {
    private Timestamp time;
    private String id;
    private String name;
    private boolean isCheckin;
    public TimeInOutOfFireStore(){}

    public TimeInOutOfFireStore(Timestamp time, String id, String name, boolean isCheckin) {
        this.time = time;
        this.id = id;
        this.name = name;
        this.isCheckin = isCheckin;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheckin() {
        return isCheckin;
    }

    public void setCheckin(boolean checkin) {
        isCheckin = checkin;
    }
}
