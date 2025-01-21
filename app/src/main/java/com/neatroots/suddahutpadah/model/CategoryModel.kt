package com.neatroots.suddahutpadah.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    val id: String = "",
    val image: String = "",
    val categoryName: String = ""

): Parcelable