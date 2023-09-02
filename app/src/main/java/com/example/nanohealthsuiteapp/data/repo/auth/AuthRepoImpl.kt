package com.example.nanohealthsuiteapp.data.repo.auth

import com.example.nanohealthsuiteapp.data.remote.ProductsApi
import com.example.nanohealthsuiteapp.data.remote.dto.auth.LoginResponse
import com.example.nanohealthsuiteapp.domain.repo.auth.AuthRepo
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val productsApi: ProductsApi
) : AuthRepo {


    override suspend fun userLogin(username: String, password: String): LoginResponse = productsApi.userLogin(username, password)

}