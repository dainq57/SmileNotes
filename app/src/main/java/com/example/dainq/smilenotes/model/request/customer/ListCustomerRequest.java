package com.example.dainq.smilenotes.model.request.customer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListCustomerRequest {
    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("listLevel")
    @Expose
    private ArrayList<String> listLevel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getListLevel() {
        return listLevel;
    }

    public void setListLevel(ArrayList<String> listLevel) {
        this.listLevel = listLevel;
    }
}
