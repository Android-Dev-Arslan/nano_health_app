package com.example.nanohealthsuiteapp.presentation.product_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nanohealthsuiteapp.R
import com.example.nanohealthsuiteapp.common.Constants
import com.example.nanohealthsuiteapp.common.Constants.PRODUCT_ID
import com.example.nanohealthsuiteapp.common.Constants.USER_TOKEN
import com.example.nanohealthsuiteapp.common.Resource
import com.example.nanohealthsuiteapp.presentation.adapters.ProductsAdapter
import com.example.nanohealthsuiteapp.utils.SharedPrefHelper
import com.example.nanohealthsuiteapp.utils.makeInvisible
import com.example.nanohealthsuiteapp.utils.makeVisible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsListFragment : Fragment() {

    private var productsRecyclerView: RecyclerView? = null
    private var productsProgressBar: ProgressBar? = null
    private val productsAdapter = ProductsAdapter()

    private val productListViewModel: ProductListViewModel by lazy {
        ViewModelProvider(this)[ProductListViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SharedPrefHelper.readString(requireContext(), USER_TOKEN).isNullOrEmpty()) {
            findNavController().navigate(R.id.action_productsListFragment_to_loginFragment)
        }

        init()
        initListeners()
    }

    private fun init() {
        productsRecyclerView = requireView().findViewById(R.id.rv_product)
        productsProgressBar = requireView().findViewById(R.id.products_progress)

        productsRecyclerView?.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.adapter = productsAdapter
        }

        productListViewModel.getProductsList()
    }

    private fun initListeners() {

        productsAdapter.onItemClick = { productDetail ->
            val args = Bundle()
            args.putString(PRODUCT_ID, productDetail.id.toString())
            findNavController().navigate(R.id.action_productsListFragment_to_productDetailFragment, args)
        }

        lifecycleScope.launch(Dispatchers.Main) {
            productListViewModel.productListFlow.collect {
                when (it) {
                    is Resource.Error -> {
                        productsProgressBar?.makeInvisible()
                        Snackbar.make(
                            requireContext(),
                            requireView(),
                            it.message ?: Constants.SOMETHING_WRONG,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Loading -> {
                        productsProgressBar?.makeVisible()
                    }

                    is Resource.Success -> {
                        productsProgressBar?.makeInvisible()
                        if (!it.data.isNullOrEmpty()) {
                            productsAdapter.updateProductData(it.data)
                        } else {
                            Snackbar.make(
                                requireContext(),
                                requireView(),
                                Constants.SOMETHING_WRONG,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}