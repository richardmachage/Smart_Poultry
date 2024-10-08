package com.forsythe.smartpoultry.presentation.composables.others

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.presentation.destinations.AnalyticsScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.BlockCellScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.EggScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.HomeScreenDestination
import com.forsythe.smartpoultry.presentation.uiModels.BottomNavigationItem

@Composable
fun MyBottomNavBar(
    navController: NavController,
    eggCollectionAccess: Boolean
) {

    val items =
        if (eggCollectionAccess) listOf<BottomNavigationItem>(
            BottomNavigationItem(
                route = HomeScreenDestination.route,
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            ),
            BottomNavigationItem(
                route = AnalyticsScreenDestination.route,
                title = "Analytics",
                selectedIcon = ImageVector.vectorResource(R.drawable.analytics_filled),
                unselectedIcon = ImageVector.vectorResource(R.drawable.analytics_outlined)
            ),
            BottomNavigationItem(
                route = BlockCellScreenDestination.route,
                title = "Blocks",//"Alerts",
                selectedIcon = Icons.Filled.Edit,//ImageVector.vectorResource(R.drawable.notification_filled),
                unselectedIcon = Icons.Outlined.Edit,//.vectorResource(R.drawable.notification_outline)
            ),

            BottomNavigationItem(
                route = EggScreenDestination.route,
                title = "Eggs",
                selectedIcon = ImageVector.vectorResource(R.drawable.egg_filled),
                unselectedIcon = ImageVector.vectorResource(R.drawable.egg_outline),
            )
        )
        else listOf<BottomNavigationItem>(
            BottomNavigationItem(
                route = HomeScreenDestination.route,
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            ),
            BottomNavigationItem(
                route = AnalyticsScreenDestination.route,
                title = "Analytics",
                selectedIcon = ImageVector.vectorResource(R.drawable.analytics_filled),
                unselectedIcon = ImageVector.vectorResource(R.drawable.analytics_outlined)
            ),
            BottomNavigationItem(
                route = BlockCellScreenDestination.route,
                title = "Blocks & Cells ",//"Alerts",
                selectedIcon = Icons.Filled.Edit,//ImageVector.vectorResource(R.drawable.notification_filled),
                unselectedIcon = Icons.Outlined.Edit,//.vectorResource(R.drawable.notification_outline)
            ),

            )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        items.forEachIndexed { _, item ->
            var selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,  //navController.currentDestination?.route == item.route,//
                onClick = {

                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true

                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )

        }
    }

}

