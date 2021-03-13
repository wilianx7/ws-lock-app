package com.unesc.wslock.dtos;

public class AuthDTO {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toJson() {
        return "{"
                + "\"login\": \"" + this.login + "\","
                + "\"password\": \"" + this.password
                + "\"}";
    }
}
