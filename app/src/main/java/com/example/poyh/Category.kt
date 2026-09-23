package com.example.poyh

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class Category(
    val id: String,
    val name: String,
    @DrawableRes val iconResId: Int,
    @ColorInt val backgroundColor: Int
)
