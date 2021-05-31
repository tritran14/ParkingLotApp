package com.example.parkinglotapp.model;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String id;
    private String name;
    private String licensePlateNumber;
    private String licenseNumber;
    private String token;
    private boolean haveNotification;
    private boolean admin;

    public Person(){}

    public Person(String id,String name, String licensePlateNumber, String licenseNumber) {
        this.id=id;
        this.name = name;
        this.licensePlateNumber = licensePlateNumber;
        this.licenseNumber = licenseNumber;
        this.haveNotification=false;
        this.admin=false;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isHaveNotification() {
        return haveNotification;
    }

    public void setHaveNotification(boolean haveNotification) {
        this.haveNotification = haveNotification;
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

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

}
