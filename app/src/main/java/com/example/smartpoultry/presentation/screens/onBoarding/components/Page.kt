package com.example.smartpoultry.presentation.screens.onBoarding.components

import androidx.annotation.DrawableRes

data class Page(
    val tittle : String,
    val description : String,
    @DrawableRes val image : Int
)