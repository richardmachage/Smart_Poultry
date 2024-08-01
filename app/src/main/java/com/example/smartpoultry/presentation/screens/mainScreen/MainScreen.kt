package com.example.smartpoultry.presentation.screens.mainScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.smartpoultry.presentation.composables.others.MyBottomNavBar
import com.example.smartpoultry.presentation.composables.others.MyTopAppBar
import com.example.smartpoultry.presentation.navigation.BottomNavGraph
import com.example.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator
){

    val mainViewModel = hiltViewModel<MainScreenViewModel>()
    val navController = rememberNavController()

    SmartPoultryTheme {
        Scaffold (
            topBar = { MyTopAppBar(navController, navigator) },
            bottomBar = { MyBottomNavBar(navController, mainViewModel.getEggCollectionAccess()) }
        ){ paddingValues ->
            BottomNavGraph(
                modifier = Modifier
                    .padding(paddingValues = paddingValues),
                navController = navController,
                navigator = navigator)
        }
    }
}