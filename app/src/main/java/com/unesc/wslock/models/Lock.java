package com.unesc.wslock.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private LockHistory last_lock_history;
    private List<LockHistory> lock_histories;
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

    public LockHistory getLastLockHistory() {
        return last_lock_history;
    }

    public void setLastLockHistory(LockHistory last_lock_history) {
        this.last_lock_history = last_lock_history;
    }

    public List<LockHistory> getLock_histories() {
        return lock_histories;
    }

    public void setLockHistories(List<LockHistory> lock_histories) {
        this.lock_histories = lock_histories;
    }

    public String toJson() throws JSONException {
        return "{\"lock_data\": {"
                + "\"id\": \"" + this.id + "\","
                + "\"name\": \"" + this.name + "\","
                + "\"mac_address\": \"" + this.mac_address + "\","
                + "\"users\": " + this.stringifyUsers()
                + "}"
                + "}";
    }

    public String getJsonMacAddress() {
        return "{\"mac_address\": \"" + this.mac_address + "\"}";
    }

    private String stringifyUsers() throws JSONException {
        if (this.getUsers() == null) {
            return "[]";
        }

        JSONArray formattedUsers = new JSONArray();

        for (User user : this.getUsers()) {
            JSONObject obj = new JSONObject();

            obj.put("id", user.getId());

            formattedUsers.put(obj);
        }

        return formattedUsers.toString();
    }
}
