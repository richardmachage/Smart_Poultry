package com.example.smartpoultry.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartpoultry.presentation.destinations.AlertScreenDestination
import com.example.smartpoultry.presentation.destinations.AnalyticsScreenDestination
import com.example.smartpoultry.presentation.destinations.BlockCellScreenDestination
import com.example.smartpoultry.presentation.destinations.EggScreenDestination
import com.example.smartpoultry.presentation.destinations.HomeScreenDestination
import com.example.smartpoultry.presentation.screens.alerts.AlertScreen
import com.example.smartpoultry.presentation.screens.analytics.AnalyticsScreen
import com.example.smartpoultry.presentation.screens.blockCellScreen.BlockCellScreen
import com.example.smartpoultry.presentation.screens.eggCollection.EggScreen
import com.example.smartpoultry.presentation.screens.home.HomeScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier : Modifier,
    navigator : DestinationsNavigator
){
    NavHost(navController = navController, startDestination = HomeScreenDestination.route , modifier = modifier){
        composable(route = HomeScreenDestination.route){ HomeScreen(navigator) }
        composable(route = AnalyticsScreenDestination.route){ AnalyticsScreen(navigator) }
        composable(route = AlertScreenDestination.route){ AlertScreen() }
        composable(route = EggScreenDestination.route){ EggScreen(navigator) }
        composable(route = BlockCellScreenDestination.route){ BlockCellScreen(navigator)}
    }
}