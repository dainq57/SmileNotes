package com.example.dainq.smilenotes.controllers.api;

import com.example.dainq.smilenotes.model.request.CustomerRequest;
import com.example.dainq.smilenotes.model.response.CustomerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APICustomer {
    @POST("add")
    Call<CustomerResponse> createCustomer(@Body CustomerRequest request, @Header("Authorization") String token);

    @POST("update")
    Call<CustomerResponse> updateCustomer(@Body CustomerRequest request, @Header("Authorization") String token);
}
