package com.example.smartpoultry.presentation.screens.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.destinations.LogInScreenDestination
import com.example.smartpoultry.presentation.destinations.MainScreenDestination
import com.example.smartpoultry.presentation.destinations.SplashScreenDestination
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination(start = true)
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
) {
    val splashViewmodel = hiltViewModel<SplashViewmodel>()

    LaunchedEffect(key1 = splashViewmodel.isLoggedIn ){
        if (splashViewmodel.isLoggedIn){
            navigator.navigate(MainScreenDestination){
                popUpTo(SplashScreenDestination){inclusive = true}
            }
        }else{
            navigator.navigate(LogInScreenDestination){
                popUpTo(SplashScreenDestination){inclusive = true}
            }
        }
    }

    SmartPoultryTheme {
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Image(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                //.padding(8.dp),
                painter = painterResource(id = (if (isSystemInDarkTheme()) R.drawable.chicken_white else R.drawable.chicken)),
                contentDescription = "chicken",
                contentScale = ContentScale.Fit
            )
        }
    }
}
