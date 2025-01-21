package com.neatroots.suddahutpadah.model

data class CartModel(
    val id: String = "",
    val productId: String = "",
    val productImage: String = "",
    val title: String = "",
    var qty: String = "",
    var price: String = "",
    var originalPrice: String = "",
    val status: String = "",
    var orderDate: String = "",
    val deliveryDate: String = ""
)
