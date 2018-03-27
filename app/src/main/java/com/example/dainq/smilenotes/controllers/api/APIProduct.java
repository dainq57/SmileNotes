package com.example.dainq.smilenotes.controllers.api;

import com.example.dainq.smilenotes.model.request.product.ProductRequest;
import com.example.dainq.smilenotes.model.response.RemoveResponse;
import com.example.dainq.smilenotes.model.response.product.ListProductResponse;
import com.example.dainq.smilenotes.model.response.product.ProductResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIProduct {
    @POST("add")
    Call<ProductResponse> addNewProduct(@Body ProductRequest request, @Header("Authorization") String token);

    @POST("update")
    Call<ProductResponse> updateProduct(@Body ProductRequest request, @Header("Authorization") String token);

    @GET("remove")
    Call<RemoveResponse> removeProduct(@Query("user-id") String userId, @Query("customer-id") String customerId,
                                       @Query("product-id") String productId, @Header("Authorization") String token);

    @GET("get-by-key")
    Call<ProductResponse> getProduct(@Query("user-id") String userId, @Query("customer-id") String customerId,
                                     @Query("product-id") String productId, @Header("Authorization") String token);

    @GET("get-by-customer-id")
    Call<ListProductResponse> getListProduct(@Query("user-id") String userId, @Query("customer-id") String customerId,
                                                             @Header("Authorization") String token);
}
