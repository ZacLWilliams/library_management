package com.source;

public class User {
    private String username;
    private String passUncrypt;

    User(String inputUsername, String inputPassword) {
        username = inputUsername;
        passUncrypt = inputPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return passUncrypt;
    }
}
