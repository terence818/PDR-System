package com.example.pdrsystem;

public class State {

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public State(String state) {
        this.state = state;
    }

    private String state;

    public State(){
        this.state="";
    }
}
