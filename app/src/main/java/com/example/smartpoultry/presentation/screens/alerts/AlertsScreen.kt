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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.presentation.composables.MyBorderedRow
import com.example.smartpoultry.presentation.composables.MyInputDialog
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

    var showDeleteDialog by remember { mutableStateOf(false) }
    MyInputDialog(
        showDialog = showDeleteDialog,
        title = "Delete Alert",
        onConfirm = {
            alertsViewModel.onDeleteAlert(alertsViewModel.selectedAlertId)
            showDeleteDialog = false
        },
        onDismiss = {showDeleteDialog = false}
    )
    {
     Text(text = "Alert will be deleted from history, confirm to proceed")
    }
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
                            .padding(6.dp),
                    ) {
                        Column {
                            Text(text = "Date : ${SimpleDateFormat("dd MMM, yyyy").format(alert.date)}")
                            Text(text = "Cell : ${alert.cellNum}")
                            Text(text = "Block : ${alert.blockNum}")
                        }

                            if (!alert.attended){
                                //Text(text = "Mark attended")
                                IconButton(onClick = { alertsViewModel.onMarkAttended(true, alert.alertId) }) {
                                    Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "Check" )
                                }
                            }else{
                                IconButton(onClick = { alertsViewModel.onMarkAttended(false, alert.alertId) }) {
                                    Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = "Check" )
                                }
                            }


                        IconButton(onClick = {
                            alertsViewModel.selectedAlertId = alert.alertId
                            showDeleteDialog = true
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "delete" )
                        }

                    }
                }
            }
        }
    }

}