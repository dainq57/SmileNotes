package com.example.dainq.smilenotes.model.response.customer;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberCustomerResponse {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private LevelData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LevelData getData() {
        return data;
    }

    public void setData(LevelData data) {
        this.data = data;
    }
}
