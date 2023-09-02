package com.example.nanohealthsuiteapp.domain.use_cases.auth

import com.example.nanohealthsuiteapp.common.Constants.API_ERROR
import com.example.nanohealthsuiteapp.common.Constants.NETWORK_ERROR
import com.example.nanohealthsuiteapp.common.Resource
import com.example.nanohealthsuiteapp.data.remote.dto.auth.LoginResponse
import com.example.nanohealthsuiteapp.domain.repo.auth.AuthRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {

    operator fun invoke(username: String, password: String): Flow<Resource<LoginResponse?>> = flow {
        try {
            emit(Resource.Loading())
            val loginResponse = authRepo.userLogin(username, password)
            emit(Resource.Success(loginResponse))
        } catch (httpException: HttpException) {
            emit(Resource.Error(httpException.localizedMessage ?: API_ERROR))
        } catch (ioException: IOException) {
            emit(Resource.Error(NETWORK_ERROR))
        }
    }
}