package com.example.nanohealthsuiteapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nanohealthsuiteapp.R
import com.example.nanohealthsuiteapp.data.remote.dto.product.ProductDetail

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    private val productsList = mutableListOf<ProductDetail>()

    var onItemClick: ((ProductDetail) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(productsList[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    fun updateProductData(productList: List<ProductDetail>) {
        this.productsList.addAll(productList)
        notifyDataSetChanged()
    }

    class ProductsViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivProductImage: ImageView? = null
        private var tvProductName: TextView? = null
        private var tvProductPrice: TextView? = null
        private var tvProductDesc: TextView? = null
        private var productRatingBar: RatingBar? = null

        fun bind(product: ProductDetail, onItemClick: ((ProductDetail) -> Unit)?) {

            ivProductImage = itemView.findViewById(R.id.iv_product_image)
            tvProductName = itemView.findViewById(R.id.tv_product_name)
            tvProductPrice = itemView.findViewById(R.id.tv_product_price)
            tvProductDesc = itemView.findViewById(R.id.tv_product_desc)
            productRatingBar = itemView.findViewById(R.id.rb_product_rating)


            Glide.with(itemView.context)
                .load(product.image)
                .into(ivProductImage!!)
            tvProductName?.text = product.title
            tvProductPrice?.text = "AED ${product.price}"
            tvProductDesc?.text = product.description
            productRatingBar?.rating = product.rating.rate.toFloat()
            itemView.setOnClickListener {
                onItemClick?.invoke(product)
            }
        }
    }
}