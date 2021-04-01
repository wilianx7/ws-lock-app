package com.unesc.wslock.models;

import java.io.Serializable;
import java.util.Date;

public class LockHistory implements Serializable {
    private int user_id;
    private int lock_id;
    private String description;
    private Date created_at;
    private Date updated_at;

    private Lock lock;
    private User user;

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getLock_id() {
        return lock_id;
    }

    public void setLockId(int lock_id) {
        this.lock_id = lock_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
