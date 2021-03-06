package com.example.dainq.smilenotes.controllers.api;

import com.example.dainq.smilenotes.model.request.user.UserChangePassRequest;
import com.example.dainq.smilenotes.model.request.user.UserRequest;
import com.example.dainq.smilenotes.model.response.user.UserChangePassResponse;
import com.example.dainq.smilenotes.model.response.user.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIUser {

    @POST("sign-up")
    Call<UserResponse> signUp(@Body UserRequest request);

    @POST("login")
    Call<UserResponse> logIn(@Body UserRequest request);

    @POST("update-info")
    Call<UserResponse> updateInfo(@Body UserRequest request, @Header("Authorization") String token);

    @POST("change-password")
    Call<UserChangePassResponse> changePassword(@Body UserChangePassRequest request, @Header("Authorization") String token);
}
