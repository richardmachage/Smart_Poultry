package com.forsythe.smartpoultry.presentation.screens.mainScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.forsythe.smartpoultry.presentation.composables.others.MyBottomNavBar
import com.forsythe.smartpoultry.presentation.composables.others.MyTopAppBar
import com.forsythe.smartpoultry.presentation.navigation.BottomNavGraph
import com.forsythe.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Destination//(start = true)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    onBackPressed: () -> Unit = {}
) {

    val mainViewModel = hiltViewModel<MainScreenViewModel>()
    val navController = rememberNavController()
    val context = LocalContext.current


    SmartPoultryTheme {
        Scaffold(
            topBar = { MyTopAppBar(navController, navigator) },
            bottomBar = { MyBottomNavBar(navController, mainViewModel.getEggCollectionAccess()) }
        ) { paddingValues ->
            BottomNavGraph(
                modifier = Modifier
                    .padding(paddingValues = paddingValues),
                navController = navController,
                navigator = navigator
            )
        }
    }
}