package com.unesc.wslock.models.lists;

import com.google.gson.annotations.SerializedName;
import com.unesc.wslock.models.LockHistory;

import java.io.Serializable;
import java.util.List;

public class LockHistoryList implements Serializable {
    @SerializedName("data")
    public List<LockHistory> lockHistories;
}
