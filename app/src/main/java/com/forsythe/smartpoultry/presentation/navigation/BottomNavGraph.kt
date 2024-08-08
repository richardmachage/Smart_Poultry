package com.forsythe.smartpoultry.presentation.navigation

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
import com.forsythe.smartpoultry.presentation.destinations.AlertScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.AnalyticsScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.BlockCellScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.EggScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.HomeScreenDestination
import com.forsythe.smartpoultry.presentation.screens.alerts.AlertScreen
import com.forsythe.smartpoultry.presentation.screens.analytics.AnalyticsScreen
import com.forsythe.smartpoultry.presentation.screens.blockCellScreen.BlockCellScreen
import com.forsythe.smartpoultry.presentation.screens.eggCollection.EggScreen
import com.forsythe.smartpoultry.presentation.screens.home.HomeScreen
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
            exitTransition = {
                exitAnime()
            }

        )
        { HomeScreen(navigator) }
        composable(
            route = AnalyticsScreenDestination.route,
            exitTransition = {
                exitAnime()
            }
        ){ AnalyticsScreen(navigator) }
        composable(
            route = AlertScreenDestination.route,

            exitTransition = {
                exitAnime()
            }){ AlertScreen() }
        composable(
            route = EggScreenDestination.route,
            exitTransition = {
                exitAnime()
            }){ EggScreen(navigator) }
        composable(
            route = BlockCellScreenDestination.route,
            exitTransition = {
                exitAnime()
            }){ BlockCellScreen(navigator)}
    }
}


fun enterAnime():EnterTransition{
    return slideInHorizontally (
        initialOffsetX = {-it},
        animationSpec = tween(
            200,
            easing = EaseIn
        )
    )
}

fun exitAnime():ExitTransition{
    return slideOutHorizontally (
        targetOffsetX = {-it},
        animationSpec = tween(
            200,
            easing = EaseOut
        )
    )
}