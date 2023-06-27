package com.example.mytest;

public class User {
    private int id;
    private String email;
    private String userType;
    private String password;

    public User(String email, String userType, String password) {
        this.email = email;
        this.userType = userType;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public String getPassword() {
        return password;
    }
}
