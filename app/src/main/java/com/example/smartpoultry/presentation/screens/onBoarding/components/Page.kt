package com.example.smartpoultry.presentation.screens.onBoarding.components

import androidx.annotation.DrawableRes
import com.example.smartpoultry.R

data class Page(
    val title : String,
    val description : String,
    @DrawableRes val image : Int
)

val pages = listOf(
    Page(
        title = "Track Production",
        description = "Easily track daily production and precisely to individual cells without a hustle",
        image = R.drawable.chicken_in_cages

    ),
    Page(
        title = "Data Integrity",
        description = "Different levels of access and specific permissions to the different farm employees depending on your organisation",
        image = R.drawable.farm_employees

    ),
    Page(
        title = "Get Insights",
        description = "Get useful insights from the collected data in form of appealing graphs. With several levels of customization to get precise intel",
        image = R.drawable.insights

    ),
    Page(
        title = "Automated Analysis",
        description = "Set up automatic analysis and get notifications when downward trend is detected and the specific cells are flagged",
        image = R.drawable.automated_analysis

    )
)