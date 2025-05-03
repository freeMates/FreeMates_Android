package com.example.freemates_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterItem (
    val filterText: String
): Parcelable