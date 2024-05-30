package com.example.ijoin;

public class Notification {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public Notification() {
        // Default constructor required for Firebase
    }

    public Notification(String title, String message) {
        this.title = title;
        this.message = message;
    }

    // Getters and setters
    // ...
}

