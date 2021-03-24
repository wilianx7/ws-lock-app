package com.unesc.wslock.models.lists;

import com.google.gson.annotations.SerializedName;
import com.unesc.wslock.models.Lock;

import java.io.Serializable;
import java.util.List;

public class LockList implements Serializable {
    @SerializedName("data")
    public List<Lock> locks;
}
