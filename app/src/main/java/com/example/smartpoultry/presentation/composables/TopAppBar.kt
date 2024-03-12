package com.example.smartpoultry.presentation.composables

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartpoultry.R
import com.example.smartpoultry.destinations.AccountScreenDestination
import com.example.smartpoultry.destinations.AlertScreenDestination
import com.example.smartpoultry.destinations.AnalyticsScreenDestination
import com.example.smartpoultry.destinations.BlockCellScreenDestination
import com.example.smartpoultry.destinations.EggScreenDestination
import com.example.smartpoultry.destinations.FeedsScreenDestination
import com.example.smartpoultry.destinations.HomeScreenDestination
import com.example.smartpoultry.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    navController: NavController,
    navigator: DestinationsNavigator
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    TopAppBar(
        title = {
            when (currentRoute) {
                HomeScreenDestination.route -> {
                    Text(text = "Home")
                }

                AnalyticsScreenDestination.route -> {
                    Text(text = "Analytics")
                }

                AlertScreenDestination.route -> {
                    Text(text = "Alerts")
                }

                EggScreenDestination.route -> {
                    Text(text = "Egg collection")
                }

                BlockCellScreenDestination.route -> {
                    Text(text = "Blocks & Cells")
                }
            }
        },
        actions = {
            IconButton(onClick = { navigator.navigate(AlertScreenDestination) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.notification_filled),
                    contentDescription = "Notifications"
                )
            }

            IconButton(onClick = { navigator.navigate(FeedsScreenDestination) })
            {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.feeds_filled),
                    contentDescription = "Feeds"
                )
            }

            IconButton(onClick = { navigator.navigate(AccountScreenDestination) })
            {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Account Icon")
            }

            IconButton(onClick = { navigator.navigate(SettingsScreenDestination) })
            {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings Icon")
            }


        },
        scrollBehavior = scrollBehavior,
    )

}