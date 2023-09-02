package com.example.nanohealthsuiteapp.data.remote

import com.example.nanohealthsuiteapp.data.remote.dto.auth.LoginResponse
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductResponse
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductDetail
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductsApi {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("products/{id}")
    suspend fun getProductDetails(
        @Path("id") id: String
    ): ProductDetail

}