package com.example.ijoin;

import java.util.HashMap;

public class Users {
    private String userType, surname, name, email, password, dataAboutUser;
    private HashMap<String, String> volTypes = new HashMap<String, String>();

    public HashMap<String, String> getVolTypes() {
        return volTypes;
    }

    public void setVolTypes(HashMap<String, String> volTypes) {
        this.volTypes = volTypes;
    }

    public String getDataAboutUser() {
        return dataAboutUser;
    }

    public void setDataAboutUser(String dataAboutUser) {
        this.dataAboutUser = dataAboutUser;
    }

    public Users(String userType, String surname, String name, String email, String password, String dataAboutUser) {
        this.userType = userType;
        this.surname = surname;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dataAboutUser = dataAboutUser;
    }

    public Users() {
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
