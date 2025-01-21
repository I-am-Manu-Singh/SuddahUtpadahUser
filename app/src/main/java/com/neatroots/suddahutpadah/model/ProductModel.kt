package com.neatroots.suddahutpadah.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val id: String = "",
    val image: String = "",
    val title: String = "",
    val description: String = "",
    val offerPrice: String = "",
    val originalPrice: String = "",
    val category: String = "",
    val categoryId: String = "",
    val rating: String = "",
    val available: Boolean = false
): Parcelable
