package com.example.freemates_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem (
    val iconOn:  Int,
    val iconOff: Int,
    val title:   String,
    var isSelected: Boolean = false
) : Parcelable
{
    /** 편의 프로퍼티: 현재 그려야 할 아이콘 */
    val currentIcon get() = if (isSelected) iconOn else iconOff
}