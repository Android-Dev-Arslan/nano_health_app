package com.example.nanohealthsuiteapp.presentation.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanohealthsuiteapp.common.Resource
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductResponse
import com.example.nanohealthsuiteapp.domain.use_cases.products.GetProductsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productsListUseCase: GetProductsListUseCase
) : ViewModel() {


    private val _productsListFlow = MutableSharedFlow<Resource<ProductResponse?>>()
    val productListFlow = _productsListFlow.asSharedFlow()


    fun getProductsList() {
        viewModelScope.launch(Dispatchers.IO) {
            productsListUseCase().collect {
                _productsListFlow.emit(it)
            }

        }
    }

}