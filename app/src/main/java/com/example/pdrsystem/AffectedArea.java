package com.example.pdrsystem;

public class AffectedArea {

    private String status;
    private String area_name;
    private String created_date;
    private String lastUpdate_date;
    private String state_name;
    private long item_bed_sheets;
    private long item_drinking_water;

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLastUpdate_date() {
        return lastUpdate_date;
    }

    public void setLastUpdate_date(String lastUpdate_date) {
        this.lastUpdate_date = lastUpdate_date;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String water_level;
    private String resource;

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

    public String getShelter() {
        return shelter;
    }

    public void setShelter(String shelter) {
        this.shelter = shelter;
    }

    private String severity;
    private String shelter;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String latitude;
    private String longitude;
    private String id;

    public AffectedArea(String status, String area_name, String state_name, long item_bed_sheets, long item_drinking_water, String water_level, String resource, String severity, String shelter, String latitude, String longitude, String id) {
        this.status = status;
        this.area_name = area_name;
        this.state_name = state_name;
        this.item_bed_sheets = item_bed_sheets;
        this.item_drinking_water = item_drinking_water;
        this.water_level = water_level;
        this.resource = resource;
        this.severity = severity;
        this.shelter = shelter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }



    public AffectedArea() {
        this.status = "";
        this.area_name = "";
        this.state_name = "";
        this.item_bed_sheets = 0;
        this.item_drinking_water = 0;
        this.id = "";
        this.latitude="0";
        this.longitude="0";
        this.water_level="0";;
        this.resource="0";;
        this.severity="0";;
        this.status="0";;
        this.shelter="0";;
        this.address="";;
        this.created_date="";
        this.lastUpdate_date="";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public long getItem_bed_sheets() {
        return item_bed_sheets;
    }

    public void setItem_bed_sheets(long item_bed_sheets) {
        this.item_bed_sheets = item_bed_sheets;
    }

    public long getItem_drinking_water() {
        return item_drinking_water;
    }

    public void setItem_drinking_water(long item_drinking_water) {
        this.item_drinking_water = item_drinking_water;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString()
    {
        return( this.area_name );
    }



}
