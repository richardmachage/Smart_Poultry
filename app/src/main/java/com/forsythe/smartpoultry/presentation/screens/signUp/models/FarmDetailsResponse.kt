package com.forsythe.smartpoultry.presentation.screens.signUp.models

import com.forsythe.smartpoultry.utils.Countries

data class FarmDetailsResponse(
    var farmName : String,
    var country : Countries?,
)

fun FarmDetailsResponse.isNoEmptyField() : Boolean {
    return farmName.isNotBlank() && country != null
}
