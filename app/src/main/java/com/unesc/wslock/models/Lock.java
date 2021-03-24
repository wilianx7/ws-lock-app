package com.unesc.wslock.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Lock implements Serializable {
    private int id;
    private int created_by_user_id;
    private String name;
    private String mac_address;
    private String state; // TODO: Check for Enum implementation
    private Date created_at;
    private Date updated_at;

    private User created_by_user;
    private List<User> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatedByUserId() {
        return created_by_user_id;
    }

    public void setCreatedByUserId(int created_by_user_id) {
        this.created_by_user_id = created_by_user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return mac_address;
    }

    public void setMacAddress(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }

    public User getCreatedByUser() {
        return created_by_user;
    }

    public void setCreatedByUser(User created_by_user) {
        this.created_by_user = created_by_user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
