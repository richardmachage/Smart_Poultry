package com.example.smartpoultry

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
import com.example.smartpoultry.presentation.screens.composables.MyBottomNavBar
import com.example.smartpoultry.presentation.screens.composables.MyCardInventory
import com.example.smartpoultry.presentation.screens.composables.MyTopAppBar
import com.example.smartpoultry.presentation.screens.eggCollection.EggScreen
import com.example.smartpoultry.presentation.screens.home.HomeScreen
import com.example.smartpoultry.presentation.screens.logIn.LogInScreen
import com.example.smartpoultry.presentation.screens.signUp.SignUpScreen
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme


class MainActivity : ComponentActivity() {
    //@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartPoultryTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {

                Scaffold(
                    topBar = {MyTopAppBar()},
                       bottomBar = { MyBottomNavBar()},

                    ) {paddingValues ->

                    }

                //}


            }
        }
    }

}

