package com.example.nanohealthsuiteapp.domain.use_cases.products

import com.example.nanohealthsuiteapp.common.Constants
import com.example.nanohealthsuiteapp.common.Resource
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductResponse
import com.example.nanohealthsuiteapp.domain.repo.product.ProductRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductsListUseCase @Inject constructor(
    private val productRepo: ProductRepo
) {

    operator fun invoke(): Flow<Resource<ProductResponse?>> = flow {
        try {
            emit(Resource.Loading())
            val productsResponse = productRepo.getProducts()
            emit(Resource.Success(productsResponse))
        } catch (httpException: HttpException) {
            emit(Resource.Error(httpException.localizedMessage ?: Constants.API_ERROR))
        } catch (ioException: IOException) {
            emit(Resource.Error(Constants.NETWORK_ERROR))
        }
    }
}