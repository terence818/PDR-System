package com.example.pdrsystem;

public class History {
    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getBlanket() {
        return blanket;
    }

    public void setBlanket(String blanket) {
        this.blanket = blanket;
    }

    public History(String created_date, String state, String area, String water, String blanket, String approver, String mobile) {
        this.created_date = created_date;
        this.state = state;
        this.area = area;
        this.water = water;
        this.blanket = blanket;
        this.approver=approver;
        this.mobile=mobile;
    }

    public History() {
        this.created_date = "";
        this.state = "";
        this.area = "";
        this.water = "";
        this.blanket = "";
        this.approver="";
        this.mobile="";
    }

    private String created_date;

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String approver;
    private String state;
    private String area;
    private String water;
    private String blanket;
    private String mobile;


}
