package com.example.nanohealthsuiteapp.domain.repo.product

import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductResponse
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductDetail

interface ProductRepo {

    suspend fun getProducts(): ProductResponse

    suspend fun getProductDetails(productId:String): ProductDetail
}