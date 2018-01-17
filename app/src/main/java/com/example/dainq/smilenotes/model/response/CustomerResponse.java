package com.example.dainq.smilenotes.model.response;

import com.example.dainq.smilenotes.model.request.CustomerRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerResponse {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private CustomerRequest data;

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

    public CustomerRequest getData() {
        return data;
    }

    public void setData(CustomerRequest data) {
        this.data = data;
    }
}
