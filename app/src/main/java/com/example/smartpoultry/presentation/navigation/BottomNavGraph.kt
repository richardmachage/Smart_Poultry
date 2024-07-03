package com.example.smartpoultry.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
        composable(
            route = HomeScreenDestination.route,
            enterTransition = {
                enterAnime()
            },
            exitTransition = {
               exitAnime()
            }
        )
        { HomeScreen(navigator) }
        composable(
            route = AnalyticsScreenDestination.route,
            enterTransition = {
                enterAnime()
            },
            exitTransition = {
                exitAnime()
            }
        ){ AnalyticsScreen(navigator) }
        composable(
            route = AlertScreenDestination.route,
            enterTransition = {
                enterAnime()
            },
            exitTransition = {
                exitAnime()
            }){ AlertScreen() }
        composable(
            route = EggScreenDestination.route,
            enterTransition = {
                enterAnime()
            },
            exitTransition = {
                exitAnime()
            }){ EggScreen(navigator) }
        composable(
            route = BlockCellScreenDestination.route,
            enterTransition = {
                enterAnime()
            },
            exitTransition = {
                exitAnime()
            }){ BlockCellScreen(navigator)}
    }
}


fun enterAnime():EnterTransition{
    return slideInHorizontally (
        initialOffsetX = {-it},
        animationSpec = tween(
            300,
            easing = EaseIn
        )
    )
}

fun exitAnime():ExitTransition{
    return slideOutHorizontally (
        targetOffsetX = {-it},
        animationSpec = tween(
            300,
            easing = EaseOut
        )
    )
}