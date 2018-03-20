package com.example.dainq.smilenotes.controllers.api;

import com.example.dainq.smilenotes.model.request.CustomerRequest;
import com.example.dainq.smilenotes.model.response.CustomerResponse;
import com.example.dainq.smilenotes.model.response.ListCustomerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APICustomer {
    @POST("add")
    Call<CustomerResponse> createCustomer(@Body CustomerRequest request, @Header("Authorization") String token);

    @POST("update")
    Call<CustomerResponse> updateCustomer(@Body CustomerRequest request, @Header("Authorization") String token);

    @GET("get-by-user-id")
    Call<ListCustomerResponse> getListCustomerByUserId(@Query("user-id") String userId, @Header("Authorization") String token);

    @GET("get-by-level")
    Call<ListCustomerResponse> getListCustomerByLevel(@Query("user-id") String userId, @Query("level") int level,
                                                      @Header("Authorization") String token);
}
