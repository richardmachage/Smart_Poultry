package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.screens.NavGraphs
import com.example.smartpoultry.presentation.screens.appCurrentDestinationAsState
import com.example.smartpoultry.presentation.screens.destinations.AlertScreenDestination
import com.example.smartpoultry.presentation.screens.destinations.AnalyticsScreenDestination
import com.example.smartpoultry.presentation.screens.destinations.EggScreenDestination
import com.example.smartpoultry.presentation.screens.destinations.HomeScreenDestination
import com.example.smartpoultry.presentation.screens.eggCollection.EggScreen
import com.example.smartpoultry.presentation.screens.startAppDestination
import com.example.smartpoultry.presentation.uiModels.BottomNavigationItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.navigateTo

@Composable
fun MyBottomNavBar(
    navController: NavController
) {
    val items = listOf<BottomNavigationItem>(
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
            route = AlertScreenDestination.route,
            title = "Alerts",
            selectedIcon = ImageVector.vectorResource(R.drawable.notification_filled),
            unselectedIcon = ImageVector.vectorResource(R.drawable.notification_outline)
        ),
        BottomNavigationItem(
            route = EggScreenDestination.route,
            title = "Eggs",
            selectedIcon = ImageVector.vectorResource(R.drawable.egg_filled),
            unselectedIcon = ImageVector.vectorResource(R.drawable.egg_outline),
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEachIndexed { index, item ->
            var selected = currentRoute == item.route
            NavigationBarItem(
                selected = selectedItemIndex == index,//selected,  //navController.currentDestination?.route == item.route,//
                onClick = {
                    selectedItemIndex = index
                    //Navigate and avoid creating multiple instances of the destination
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop =true
                       // launchSingleTop = true // Avoid multiple copies of the same destination
                        //restoreState = true // Restore state when reselecting a previously selected item

                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
//                        popUpTo(navController.graph.startDestinationId){
//                            saveState = true
//                        }
                    }
                    //add code to handle navigation
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
