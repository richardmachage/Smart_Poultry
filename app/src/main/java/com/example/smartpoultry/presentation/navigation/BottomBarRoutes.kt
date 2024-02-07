package com.example.smartpoultry.presentation.navigation

sealed class BottomBarRoutes (
    val route: String
){
    data object Home : BottomBarRoutes(route = "home")
    data object Analytics : BottomBarRoutes(route = "analytics")
    data object Alerts : BottomBarRoutes(route = "alerts")
    data object Eggs : BottomBarRoutes(route = "eggs")
}