package com.neatroots.suddahutpadah.adapter

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.textfield.TextInputEditText
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.databinding.ItemCartBinding
import com.neatroots.suddahutpadah.model.CartModel

//
//class CartAdapter(val onClick: OnItemClickListener) : ListAdapter<CartModel, CartAdapter.CategoryVH>(DiffUtils) {
//    inner class CategoryVH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)
//
//    object DiffUtils : DiffUtil.ItemCallback<CartModel>() {
//        override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
//            return oldItem.productId == newItem.productId
//        }
//
//        override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
//        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CategoryVH(binding)
//    }
//
//    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
//        val item = getItem(position)
//
//        holder.binding.apply {
//
//
//            setupCounter(etQty, item.qty, btIncrease, btMinus)
//
//            ivServiceImage.load(item.productImage) {
//                placeholder(R.drawable.placeholder)
//                error(R.drawable.placeholder)
//            }
//
//            tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//
//            etQty.addTextChangedListener(object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//                    val newQuantity = s.toString().toIntOrNull() ?: 1
//
//                    item.qty = newQuantity.toString()
//                    onClick.onQuantityChanged(item)
//                    val totalPrice = item.price.toInt() * newQuantity
//                    tvTotalPrice.text = "₹ $totalPrice"
//
//                }
//
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                }
//            })
//
//            tvTitle.text = item.title
//            tvTotalPrice.text =  "₹ ${item.price}"
//            etQty.setText(item.qty)
//            tvOriginalPrice.text = "₹ ${item.originalPrice}"
//            tvOfferPrice.text = "₹ ${item.price}"
//
//            btDeleteCart.setOnClickListener {
//                onClick.removeFromCart(item)
//            }
//
//            holder.itemView.setOnClickListener {
//
//            }
//
//
//        }
//
//    }
//
//    private fun setupCounter(qty: TextInputEditText, initialQty: String, increaseButton: ImageView, decreaseButton: ImageView) {
//        var count = initialQty.toIntOrNull() ?: 1
//        Log.d("countrecheck", "setupCounter: $count")
//        qty.setText(count.toString())
//        increaseButton.setOnClickListener {
//            Log.d("countrecheck", "setupCounter: $count")
//            count++
//            qty.setText(count.toString())
//            Log.d("countrecheck", "setupCounter: $count")
//        }
//
//        decreaseButton.setOnClickListener {
//            if (count > 1) {
//                count--
//                qty.setText(count.toString())
//            }
//        }
//    }
//
//
//    interface OnItemClickListener {
//        fun removeFromCart(product: CartModel)
//        fun onQuantityChanged(product: CartModel)
//    }
//
//
//}



class CartAdapter(val onClick: OnItemClickListener) : ListAdapter<CartModel, CartAdapter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffUtils : DiffUtil.ItemCallback<CartModel>() {
        override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            setupCounter(etQty, item.qty, btIncrease, btMinus)

            ivServiceImage.load(item.productImage) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            etQty.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newQuantity = if (s.isNullOrEmpty()) {
                        1 // Default to 1 if the input is empty
                    } else {
                        s.toString().toIntOrNull() ?: 1 // Default to 1 if parsing fails
                    }

                    item.qty = newQuantity.toString()
                    onClick.onQuantityChanged(item)

                    // Calculate and update total price
                    val totalPrice = (item.price.toIntOrNull() ?: 0) * newQuantity
                    tvTotalPrice.text = "₹ $totalPrice"
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            tvTitle.text = item.title
            tvTotalPrice.text = "₹ ${item.price}"
            etQty.setText(item.qty)
            tvOriginalPrice.text = "₹ ${item.originalPrice}"
            tvOfferPrice.text = "₹ ${item.price}"

            btDeleteCart.setOnClickListener {
                onClick.removeFromCart(item)
            }
        }
    }

    private fun setupCounter(
        qty: TextInputEditText,
        initialQty: String,
        increaseButton: ImageView,
        decreaseButton: ImageView
    ) {
        var count = initialQty.toIntOrNull() ?: 1
        qty.setText(count.toString())

        increaseButton.setOnClickListener {
            count++
            qty.setText(count.toString())
        }

        decreaseButton.setOnClickListener {
            if (count > 1) {
                count--
                qty.setText(count.toString())
            }
        }
    }

    interface OnItemClickListener {
        fun removeFromCart(product: CartModel)
        fun onQuantityChanged(product: CartModel)
    }
}
