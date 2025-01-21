package com.neatroots.suddahutpadah.model

data class OrderModel(
    val id: String = "",
    val userId: String = "",
    val orderDate: String = "",
    val totalAmount: String = "",
    val orderedItems: List<CartModel> = ArrayList()
)