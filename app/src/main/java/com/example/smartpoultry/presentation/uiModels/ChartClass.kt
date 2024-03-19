package com.example.smartpoultry.presentation.uiModels

data class ChartClass1(
    val xDateValue : String,
    val yNumOfEggs : Int,
    val numOfChicken : Int,
)

data class ChartClass(
    val xDateValue : String,
    val yNumOfEggs : Int,
    val numOfChicken: Int = 0,
)
