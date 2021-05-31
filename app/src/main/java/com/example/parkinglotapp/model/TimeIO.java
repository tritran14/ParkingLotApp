package com.example.parkinglotapp.model;

import java.util.Date;

public class TimeIO {
    private boolean checkin;
    private Date time;
    private String id;
    private String name;
    private boolean admin;

    public TimeIO(){}

    public TimeIO(boolean checkin, Date time, String id, String name, boolean admin) {
        this.checkin = checkin;
        this.time = time;
        this.id = id;
        this.name = name;
        this.admin = admin;
    }

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
