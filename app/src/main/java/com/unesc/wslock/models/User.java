package com.unesc.wslock.models;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String login;
    private String email;
    private String password;

    private boolean has_lock_access;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public boolean hasLockAccess() {
        return has_lock_access;
    }

    public void setHasLockAccess(boolean has_lock_access) {
        this.has_lock_access = has_lock_access;
    }

    public String toJson() {
        return "{\"user_data\": {"
                + "\"id\": \"" + this.id + "\","
                + "\"name\": \"" + this.name + "\","
                + "\"login\": \"" + this.login + "\","
                + "\"email\": \"" + this.email + "\","
                + "\"password\": \"" + this.password
                + "\"}"
                + "}";
    }
}
