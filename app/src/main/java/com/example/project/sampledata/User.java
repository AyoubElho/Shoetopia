package com.example.project.sampledata;

public class User {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String imageUser;

    public User() {

    }

    // Constructor
    public User(String userId, String username, String email, String password, String imageUser) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.imageUser = imageUser;
    }


    // Getter methods (optional)
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUser() {
        return imageUser;
    }
}