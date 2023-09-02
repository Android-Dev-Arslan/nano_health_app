package com.example.nanohealthsuiteapp.presentation.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanohealthsuiteapp.common.Resource
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductDetail
import com.example.nanohealthsuiteapp.domain.use_cases.products.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    private val _productDetailFlow = MutableSharedFlow<Resource<ProductDetail?>>()
    val productDetailFlow = _productDetailFlow.asSharedFlow()

    fun getProductDetails(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getProductDetailsUseCase(productId).collect {
                _productDetailFlow.emit(it)
            }
        }
    }
}