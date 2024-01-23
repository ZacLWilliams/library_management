package com.source;

public class userId {
    private String username;
    private String passUncrypt;
    private String id;

    userId(String inputId, String inputUsername, String inputPassword) {
        username = inputUsername;
        passUncrypt = inputPassword;
        id = inputId;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return passUncrypt;
    }
}
