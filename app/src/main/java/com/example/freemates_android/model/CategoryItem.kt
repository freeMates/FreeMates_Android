package com.example.freemates_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem (
    val imageUrl: Int,
    val title: String
) : Parcelable