package com.example.smartpoultry.presentation.screens.feeds

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FeedsScreen(){
    val feedsViewModel = hiltViewModel<FeedsViewModel>()

}