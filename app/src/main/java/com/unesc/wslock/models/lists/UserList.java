package com.unesc.wslock.models.lists;

import com.google.gson.annotations.SerializedName;
import com.unesc.wslock.models.User;

import java.io.Serializable;
import java.util.List;

public class UserList implements Serializable {
    @SerializedName("data")
    public List<User> users;
}
