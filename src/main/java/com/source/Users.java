package com.source;

public class Users {
    private String username;
    private String passUncrypt;

    public void setUsername(String inputUsername) {
        username = inputUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String inputPassword) {
        passUncrypt = inputPassword;
    }

    public String getPassword() {
        return passUncrypt;
    }
}
