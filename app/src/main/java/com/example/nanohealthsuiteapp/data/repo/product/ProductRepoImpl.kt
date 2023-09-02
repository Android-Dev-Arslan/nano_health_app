package com.example.nanohealthsuiteapp.data.repo.product

import com.example.nanohealthsuiteapp.data.remote.ProductsApi
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductDetail
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductResponse
import com.example.nanohealthsuiteapp.domain.repo.product.ProductRepo
import javax.inject.Inject

class ProductRepoImpl @Inject constructor(
    private val productsApi: ProductsApi
) : ProductRepo {


    override suspend fun getProducts(): ProductResponse = productsApi.getProducts()

    override suspend fun getProductDetails(productId: String): ProductDetail =
        productsApi.getProductDetails(productId)


}