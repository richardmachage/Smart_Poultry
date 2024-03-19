package com.example.smartpoultry.presentation.screens.alerts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.presentation.composables.MyBorderedRow
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AlertScreen(
    //modifier:Modifier
    navigator: DestinationsNavigator
){
    val alertsViewModel = hiltViewModel<AlertsViewModel>()
    val listOfAlerts by remember { alertsViewModel.getFlaggedCells() }.collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Alerts")},
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription ="back" )
                    }
                }
            )
        }
    ) {paddingValues ->
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background,

            ){
            LazyColumn(){
                itemsIndexed(listOfAlerts){index, alert ->  
                    MyVerticalSpacer(height = 5)
                    MyBorderedRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    ) {
                        Column {
                            Text(text = "Date : ${SimpleDateFormat("dd MMM, yyyy").format(alert.date)}")
                            Text(text = "Cell : ${alert.cellNum}")
                            Text(text = "Block : ${alert.blockNum}")
                        }
                    }
                }
            }
        }
    }

}