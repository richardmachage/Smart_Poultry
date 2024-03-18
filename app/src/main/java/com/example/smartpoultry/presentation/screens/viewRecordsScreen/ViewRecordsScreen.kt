package com.example.smartpoultry.presentation.screens.viewRecordsScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ViewRecordsScreen(
    navigator: DestinationsNavigator
){
    val recordsViewModel = hiltViewModel<ViewRecordsViewModel>()
    val listOfRecords = recordsViewModel.getAllRecords().collectAsState(initial = emptyList())

    Scaffold (
        topBar = { TopAppBar(
            title = { Text(text = "Egg Collection Records") },
            navigationIcon = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back" )
                }
            },
           // scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        )
        }
    ){paddingValues ->
        Surface (
            modifier = Modifier.padding(paddingValues)
        ){
            LazyColumn(){

            }
        }
    }
}