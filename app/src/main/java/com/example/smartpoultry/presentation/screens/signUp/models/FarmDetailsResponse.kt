package com.example.smartpoultry.presentation.screens.signUp.models

data class FarmDetailsResponse(
    var farmName : String,
    var country : String,
)

fun FarmDetailsResponse.isValid() : Boolean {
    return farmName.isNotBlank() || country.isNotBlank()
}
