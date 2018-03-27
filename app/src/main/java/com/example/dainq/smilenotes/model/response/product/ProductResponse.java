package com.example.dainq.smilenotes.model.response.product;

import com.example.dainq.smilenotes.model.request.product.ProductRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private ProductRequest data;

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

    public ProductRequest getData() {
        return data;
    }

    public void setData(ProductRequest data) {
        this.data = data;
    }
}
