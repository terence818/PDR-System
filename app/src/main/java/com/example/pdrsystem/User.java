package com.example.pdrsystem;

public class User {




    private String type;
    private String email;

    public User(String type, String email, String phone_number, String user_type, String username) {
        this.type = type;
        this.email = email;
        this.phone_number = phone_number;
        this.user_type = user_type;
        this.username = username;
    }

    private String phone_number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String user_type;
    private String username;


    public User(){
        this.type = "";
        this.email = "";
        this.username = "";
        this.user_type="";
        this.phone_number="";


    }
}

