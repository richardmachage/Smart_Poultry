package com.example.smartpoultry.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.smartpoultry.domain.notifications.createNotificationChannel
import com.example.smartpoultry.presentation.NavGraphs
import com.example.smartpoultry.presentation.destinations.MainScreenDestination
import com.example.smartpoultry.presentation.destinations.OnBoardingScreenDestination
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        createNotificationChannel(
            context = applicationContext,
            channelName = "Flagged cells alerts",
            descriptionText = "Alerts for cells detected with downward trend",
            channelID = "1"
        )
        val destination = intent.getStringExtra("destination")
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setVisible(
                !mainViewModel.isLoggedIn
            )
        }

        setContent {
            Log.d("Destination", destination.toString())
            SmartPoultryTheme {
                //DestinationsNavHost(navGraph = NavGraphs.root)
                if (mainViewModel.isFirstInstall) { // first time installed
                    DestinationsNavHost(
                        startRoute = OnBoardingScreenDestination,
                        navGraph = NavGraphs.root
                    )
                } else {// not first time installed
                    if (mainViewModel.isLoggedIn) {// User Already Logged in
                        DestinationsNavHost(
                            startRoute = MainScreenDestination,
                            navGraph = NavGraphs.root
                        )
                    } else { // User not yet Logged in or User Logged Out
                        DestinationsNavHost(navGraph = NavGraphs.root)
                    }
                }
            }

        }
    }

}

/*
@Destination(start = true)
@Composable
fun LaunchScreen(
    navigator: DestinationsNavigator,
) {
    val launchViewmodel = hiltViewModel<MainViewModel>()
    val isLoggedIn by remember { mutableStateOf(launchViewmodel.isLoggedIn) }
    val isFirstInstall by remember { mutableStateOf(launchViewmodel.isFirstInstall) }

    LaunchedEffect(isFirstInstall) {
        if (isFirstInstall) {
            navigator.navigate(OnBoardingScreenDestination) {
                popUpTo(NavGraphs.root.startRoute) { inclusive = true }
            }
        } else {
            //LaunchedEffect(isLoggedIn){
            if (isLoggedIn) {
                navigator.navigate(MainScreenDestination) {
                    popUpTo(NavGraphs.root.startRoute) { inclusive = true }
                }
            } else {
                navigator.navigate(LogInScreenDestination) {
                    popUpTo(NavGraphs.root.startRoute) { inclusive = true }
                }
            }
            //isLoggedIn = false
            //}

        }
    }

}*/
