package com.example.ijoin;

import java.util.HashMap;

public class Post {
    private String postName;
    private String description;
    private String profilePictureUrl; // Profile picture URL of the company associated with the post
    private String comapnyUid;
    private String companyName;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getComapnyUid() {
        return comapnyUid;
    }

    public void setComapnyUid(String comapnyUid) {
        this.comapnyUid = comapnyUid;
    }

    private HashMap<String, String> volTypes;

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public HashMap<String, String> getVolTypes() {
        return volTypes;
    }

    public void setVolTypes(HashMap<String, String> volTypes) {
        this.volTypes = volTypes;
    }
}
