package com.example.smartpoultry.presentation.screens.mainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.smartpoultry.presentation.navigation.BottomNavGraph
import com.example.smartpoultry.presentation.screens.composables.MyBottomNavBar
import com.example.smartpoultry.presentation.screens.composables.MyTopAppBar
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun MainScreen(){
    val navController = rememberNavController()

    SmartPoultryTheme {

        Scaffold (
            topBar = { MyTopAppBar()},
            bottomBar = { MyBottomNavBar(navController)}
        ){ paddingValues ->
            BottomNavGraph(
                modifier = Modifier
                    .padding(paddingValues = paddingValues),
                navController = navController)
        }
    }
}