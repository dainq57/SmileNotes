package com.example.dainq.smilenotes.model.response;

import com.example.dainq.smilenotes.model.request.UserRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Response use for api: login, sign-up, update-info
 **/
public class UserResponse {

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private UserRequest data;

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

    public UserRequest getData() {
        return data;
    }

    public void setData(UserRequest data) {
        this.data = data;
    }
}
