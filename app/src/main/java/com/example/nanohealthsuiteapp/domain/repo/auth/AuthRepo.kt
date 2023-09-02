package com.example.nanohealthsuiteapp.domain.repo.auth

import com.example.nanohealthsuiteapp.data.remote.dto.auth.LoginResponse

interface AuthRepo {

    suspend fun userLogin(username: String, password: String): LoginResponse
}