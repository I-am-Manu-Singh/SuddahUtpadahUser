package com.neatroots.suddahutpadah.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.databinding.ItemOrderHistoryBinding
import com.neatroots.suddahutpadah.model.CartModel


class OrderHistoryAdapter : ListAdapter<CartModel, OrderHistoryAdapter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffUtils : DiffUtil.ItemCallback<CartModel>() {
        override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)

        holder.binding.apply {

            ivServiceImage.load(item.productImage) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            tvTitle.text = item.title
            tvOrderDate.text =  "Order: ${item.orderDate}"
            tvDeliveredDate.text =  "Delivered: ${item.deliveryDate}"
            tvQty.text = "Qty : ${item.qty}"
            tvOrderStatus.text = item.status
            tvPrice.text = "₹ ${item.price.toInt() * item.qty.toInt()}"

        }

    }






}







