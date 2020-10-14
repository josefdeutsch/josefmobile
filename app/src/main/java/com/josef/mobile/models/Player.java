package com.josef.mobile.models;

public class Player {

    String message;

    int id;

    public Player(String message, int id) {
        this.message = message;
        this.id = id;
    }

    public Player() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
