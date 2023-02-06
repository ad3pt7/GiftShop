package com.example.giftshop.models;

public class User {
    public int id;
    public String login;
    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(int id,String username, String email) {
        this.login = username;
        this.password = email;
    }

}