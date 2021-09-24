package com.example.pdrsystem;


public class Report {






    private String address;
    private String place;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getWater_level() {
        return water_level;
    }

    public void setWater_level(String water_level) {
        this.water_level = water_level;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShelter() {
        return shelter;
    }

    public void setShelter(String shelter) {
        this.shelter = shelter;
    }

    private String Latitude;
    private String Longitude;


    public Report(String address, String place, String latitude, String longitude, String water_level, String resource, String severity, String status, String shelter) {
        this.address = address;
        this.place = place;
        Latitude = latitude;
        Longitude = longitude;
        this.water_level = water_level;
        this.resource = resource;
        this.severity = severity;
        this.status = status;
        this.shelter = shelter;
    }
    private String water_level;
    private String resource;
    private String severity;
    private String status;
    private String shelter;



    public Report() {
        this.address = "";
        this.place = "";
        this.Latitude = "";
        this.Longitude = "";
        this.water_level="";
        this.resource="";
        this.severity="";
        this.status="";


    }

}