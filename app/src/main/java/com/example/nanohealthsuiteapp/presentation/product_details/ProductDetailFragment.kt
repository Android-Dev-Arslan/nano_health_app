package com.example.nanohealthsuiteapp.presentation.product_details

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.nanohealthsuiteapp.R
import com.example.nanohealthsuiteapp.common.Constants
import com.example.nanohealthsuiteapp.common.Constants.PRODUCT_ID
import com.example.nanohealthsuiteapp.common.Resource
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductDetail
import com.example.nanohealthsuiteapp.utils.makeInvisible
import com.example.nanohealthsuiteapp.utils.makeVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var ivBack: ImageView? = null
    private var ivMenu: ImageView? = null
    private var ivProductImage: ImageView? = null
    private var productDetailBottomSheet: ConstraintLayout? = null
    private var cvShare: CardView? = null
    private var tvOrderNow: TextView? = null
    private var tvProductName: TextView? = null
    private var tvProductDesc: TextView? = null
    private var tvReviewsCount: TextView? = null
    private var tvRating: TextView? = null
    private var ratingBar: RatingBar? = null
    private var productDetailProgress: LinearLayout? = null
    private var ivToggleBottomSheet: ImageView? = null

    private lateinit var behavior: BottomSheetBehavior<View>

    private val productDetailViewModel: ProductDetailViewModel by lazy {
        ViewModelProvider(this)[ProductDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setupBottomSheet()
        initListeners()
    }

    private fun init() {
        ivBack = requireView().findViewById(R.id.iv_back)
        ivMenu = requireView().findViewById(R.id.iv_menu)
        ivProductImage = requireView().findViewById(R.id.iv_product_image)
        cvShare = requireView().findViewById(R.id.cv_share)
        tvOrderNow = requireView().findViewById(R.id.tv_order_now)
        tvProductName = requireView().findViewById(R.id.tv_product_name)
        tvProductDesc = requireView().findViewById(R.id.tv_product_desc)
        tvReviewsCount = requireView().findViewById(R.id.tv_review_count)
        tvRating = requireView().findViewById(R.id.tv_rating)
        ratingBar = requireView().findViewById(R.id.rb_product_rating)
        productDetailBottomSheet = requireView().findViewById(R.id.bottom_sheet)
        productDetailProgress = requireView().findViewById(R.id.product_detail_progress)
        ivToggleBottomSheet = requireView().findViewById(R.id.iv_toggle_bottom_sheet)


        arguments?.getString(PRODUCT_ID)?.let {
            productDetailViewModel.getProductDetails(it)
        }


    }

    private fun initListeners() {
        ivBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            productDetailViewModel.productDetailFlow.collect {
                when (it) {
                    is Resource.Error -> {
                        productDetailProgress?.makeInvisible()
                        Snackbar.make(
                            requireContext(),
                            requireView(),
                            it.message ?: Constants.SOMETHING_WRONG,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Loading -> {
                        productDetailProgress?.makeVisible()
                    }

                    is Resource.Success -> {
                        productDetailProgress?.makeInvisible()
                        if (it.data != null) {
                            setupProductDetails(it.data)
                            productDetailBottomSheet?.makeVisible()
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

    private fun setupProductDetails(productDetail: ProductDetail) {
        Glide.with(requireContext())
            .load(productDetail.image)
            .into(ivProductImage!!)

        tvProductName?.text = productDetail.title
        tvProductDesc?.text = productDetail.description
        tvReviewsCount?.text = "${getString(R.string.reviews)} (${productDetail.rating.count})"
        ratingBar?.rating = productDetail.rating.rate.toFloat()
    }

    private fun setupBottomSheet() {
        val displayMetrics = DisplayMetrics()
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenHeight = displayMetrics.heightPixels
        val twentyPercentHeight = (screenHeight * 0.20).toInt()
        behavior = BottomSheetBehavior.from(productDetailBottomSheet!!)
        behavior.peekHeight = twentyPercentHeight

        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    ivToggleBottomSheet?.rotation = 180f
                } else {
                    ivToggleBottomSheet?.rotation = 0f
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }
}