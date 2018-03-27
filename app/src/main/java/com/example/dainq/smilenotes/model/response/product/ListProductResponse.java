package com.example.dainq.smilenotes.model.response.product;

import com.example.dainq.smilenotes.model.request.product.ProductRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListProductResponse {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private ArrayList<ProductRequest> data;

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

    public ArrayList<ProductRequest> getData() {
        return data;
    }

    public void setData(ArrayList<ProductRequest> data) {
        this.data = data;
    }
}
