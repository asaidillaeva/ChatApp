package com.example.chatapp.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class Chat implements Serializable {
    private String id;
    private List<String> userIds;

    public Chat() {
    }
    public String getId() {
        return id;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

}
