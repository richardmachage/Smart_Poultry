package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartpoultry.presentation.screens.destinations.AlertScreenDestination
import com.example.smartpoultry.presentation.screens.destinations.AnalyticsScreenDestination
import com.example.smartpoultry.presentation.screens.destinations.EggScreenDestination
import com.example.smartpoultry.presentation.screens.destinations.HomeScreenDestination


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar( navController : NavController){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    TopAppBar(
        title = {
            when(currentRoute){
              HomeScreenDestination.route -> {Text(text ="Home" )}
              AnalyticsScreenDestination.route -> {Text(text ="Analytics" )}
              AlertScreenDestination.route -> {Text(text ="Alerts" )}
              EggScreenDestination.route -> {Text(text ="Egg collection" )}
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ })
            {
                Icon(imageVector= Icons.Default.AccountCircle, contentDescription ="Account Icon" )
            }
            IconButton(onClick = { /*TODO*/ })
            {
                Icon(imageVector = Icons.Default.Settings, contentDescription ="Settings Icon")
            }
        },
        scrollBehavior = scrollBehavior,
    )

}