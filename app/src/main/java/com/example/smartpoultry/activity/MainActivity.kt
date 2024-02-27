package com.example.smartpoultry.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.smartpoultry.presentation.screens.NavGraph
import com.example.smartpoultry.presentation.screens.NavGraphs
import com.example.smartpoultry.presentation.composables.MyBottomNavBar
import com.example.smartpoultry.presentation.composables.MyCardInventory
import com.example.smartpoultry.presentation.composables.MyTopAppBar
import com.example.smartpoultry.presentation.screens.eggCollection.EggScreen
import com.example.smartpoultry.presentation.screens.home.HomeScreen
import com.example.smartpoultry.presentation.screens.logIn.LogInScreen
import com.example.smartpoultry.presentation.screens.signUp.SignUpScreen
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartPoultryTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }

}

