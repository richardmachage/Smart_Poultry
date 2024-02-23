package com.example.smartpoultry.presentation.screens.analytics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AnalyticsScreen(
    //modifier : Modifier
    ){
    val analyticsViewModel = hiltViewModel<AnalyticsViewModel>()
    val listOfBlocksWithCells by remember {analyticsViewModel.listOfBlocksWithCells}.collectAsState()

    Surface (
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Text(text = "Analytics Screen")
    }
}