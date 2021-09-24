package com.example.pdrsystem;

public class Offer {
    private String datetime;
    private String area_name;
    private String state_name;
    private long item_bed_sheets;
    private long item_drinking_water;
    private String username;
    private String id;
    private String status;

    public Offer(String datetime, String area_name, String state_name, long item_bed_sheets, long item_drinking_water, String username, String id, String status) {
        this.datetime = datetime;
        this.area_name = area_name;
        this.state_name = state_name;
        this.item_bed_sheets = item_bed_sheets;
        this.item_drinking_water = item_drinking_water;
        this.username = username;
        this.id = id;
        this.status = status;
    }

    public Offer() {
        this.datetime = "";
        this.area_name = "";
        this.state_name = "";
        this.item_bed_sheets = 0;
        this.item_drinking_water = 0;
        this.username = "";
        this.id = "";
        this.status = "";
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
