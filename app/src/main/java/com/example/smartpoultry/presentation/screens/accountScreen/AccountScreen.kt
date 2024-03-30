package com.example.smartpoultry.presentation.screens.accountScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AccountScreen(
    navigator : DestinationsNavigator
){

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Account Details")},
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        
                    }
                }
            )
        }
    ) {paddingValues->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Text(text = "Account Details Screen")
            }
        }
    }

}