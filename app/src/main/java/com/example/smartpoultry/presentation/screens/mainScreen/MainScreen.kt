package com.example.smartpoultry.presentation.screens.mainScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.smartpoultry.presentation.composables.MyBottomNavBar
import com.example.smartpoultry.presentation.composables.MyTopAppBar
import com.example.smartpoultry.presentation.navigation.BottomNavGraph
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator
){
    val mainViewModel = hiltViewModel<MainScreenViewModel>()
    val navController = rememberNavController()
    /*val userRole by remember{ mainViewModel.myDataStore.readData(USER_ROLE_KEY)}.collectAsState(
        initial = ""
    )*/
    SmartPoultryTheme {

        Scaffold (
            topBar = { MyTopAppBar(navController, navigator) },
            bottomBar = { MyBottomNavBar(navController, mainViewModel.getUserRole()) }
        ){ paddingValues ->
            BottomNavGraph(
                modifier = Modifier
                    .padding(paddingValues = paddingValues),
                navController = navController,
                navigator = navigator)
        }
    }
}