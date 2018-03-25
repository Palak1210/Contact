package com.jump360.palakbhootra.phonebook;

import java.io.Serializable;

/**
 * Created by palakbhootra on 24-03-2018.
 */

public class Contacts implements Serializable {

    private String name;
    private String phone;
    private String key;
    private double lat;
    private double longi;

    public Contacts(){

    }

    public Contacts(String name, String phone, String key, double lat, double longi) {
        this.name = name;
        this.phone = phone;
        this.key = key;
        this.lat = lat;
        this.longi = longi;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Contacts(String name, String phone,String key) {
        this.name = name;
        this.phone = phone;
        this.key = key;
    }
}