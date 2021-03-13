package com.unesc.wslock.models;

import java.io.Serializable;

public class Auth implements Serializable {
    private String access_token;
    private User user;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
