package com.example.smartpoultry.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartpoultry.NavGraphs
import com.example.smartpoultry.destinations.LogInScreenDestination
import com.example.smartpoultry.destinations.MainScreenDestination
import com.example.smartpoultry.domain.trendAnalysis.ProductionAnalysisWorker
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity (): ComponentActivity() {
    val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val workRequest =
            PeriodicWorkRequestBuilder<ProductionAnalysisWorker>(24, TimeUnit.HOURS).build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setVisible(
                !mainViewModel.isLoggedIn
            )
        }

        setContent {
            SmartPoultryTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }

}

@Destination(start = true)
@Composable
fun LaunchScreen(
    navigator: DestinationsNavigator,
){
    val launchViewmodel = hiltViewModel<MainViewModel>()
    var isLoggedIn by remember{ mutableStateOf( launchViewmodel.isLoggedIn)}

    LaunchedEffect(isLoggedIn){
        if (isLoggedIn){
            navigator.navigate(MainScreenDestination){
                popUpTo(NavGraphs.root.startRoute){inclusive=true}
            }
        } else{
            navigator.navigate(LogInScreenDestination){
                popUpTo(NavGraphs.root.startRoute){inclusive=true}
            }
        }
        //isLoggedIn = false
    }

}
