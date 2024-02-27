package com.example.smartpoultry.presentation.screens.mainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.smartpoultry.presentation.navigation.BottomNavGraph
import com.example.smartpoultry.presentation.composables.MyBottomNavBar
import com.example.smartpoultry.presentation.composables.MyTopAppBar
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator
){
    val navController = rememberNavController()

    SmartPoultryTheme {

        Scaffold (
            topBar = { MyTopAppBar(navController, navigator) },
            bottomBar = { MyBottomNavBar(navController) }
        ){ paddingValues ->
            BottomNavGraph(
                modifier = Modifier
                    .padding(paddingValues = paddingValues),
                navController = navController,
                navigator = navigator)
        }
    }
}