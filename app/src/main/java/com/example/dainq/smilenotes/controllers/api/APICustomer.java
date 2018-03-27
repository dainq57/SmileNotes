package com.example.dainq.smilenotes.controllers.api;

import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.example.dainq.smilenotes.model.request.customer.ListCustomerRequest;
import com.example.dainq.smilenotes.model.request.customer.SearchRequest;
import com.example.dainq.smilenotes.model.response.customer.CustomerResponse;
import com.example.dainq.smilenotes.model.response.customer.ListCustomerResponse;
import com.example.dainq.smilenotes.model.response.customer.NumberCustomerResponse;
import com.example.dainq.smilenotes.model.response.RemoveResponse;
import com.example.dainq.smilenotes.model.response.customer.SearchResponse;

import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("get-by-level")
    Call<ListCustomerResponse> getListCustomerByLevel(@Body ListCustomerRequest request, @Header("Authorization") String token);

    @GET("get-by-key")
    Call<CustomerResponse> getCustomer(@Query("user-id") String userId, @Query("customer-id") String customerId,
                                       @Header("Authorization") String token);

    @GET("remove")
    Call<RemoveResponse> removeCustomer(@Query("user-id") String userId, @Query("customer-id") String customerId,
                                        @Header("Authorization") String token);

    @POST("search")
    Call<SearchResponse> searchCustomer(@Body SearchRequest request, @Header("Authorization") String token);

    @GET("get-number-customer")
    Call<NumberCustomerResponse> getNumberCustomer(@Query("user-id") String userId, @Header("Authorization") String token);
}
