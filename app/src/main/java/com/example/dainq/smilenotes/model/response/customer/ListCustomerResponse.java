package com.example.dainq.smilenotes.model.response.customer;

import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListCustomerResponse {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<CustomerRequest> data;

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

    public List<CustomerRequest> getData() {
        return data;
    }

    public void setData(ArrayList<CustomerRequest> data) {
        this.data = data;
    }
}
