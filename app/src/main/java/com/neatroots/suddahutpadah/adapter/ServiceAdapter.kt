package com.neatroots.suddahutpadah.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.databinding.ItemServiceBinding
import com.neatroots.suddahutpadah.model.ProductModel
import com.neatroots.suddahutpadah.utils.Utils


class ServiceAdapter(private val onClick: OnItemClickListener) : ListAdapter<ProductModel, ServiceAdapter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffUtils : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)

        holder.binding.apply {

            ivServiceImage.load(item.image) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


            tvTitle.text = item.title
            tvDescription.text = item.description
            val dc =  Utils.calculateDiscount(item.offerPrice.toInt(), item.originalPrice.toInt())
            tvPercentage.text = "${dc.toInt()}% Off"
            tvOriginalPrice.text = "₹${item.originalPrice}"
            tvOfferPrice.text = "₹${item.offerPrice}"
            tvRating.text = item.rating



            holder.itemView.setOnClickListener {
                onClick.onItemClick(item)
            }


        }

        }

    interface OnItemClickListener {
        fun onItemClick(model: ProductModel)
    }



}







