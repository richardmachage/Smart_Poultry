package com.forsythe.smartpoultry.presentation.screens.alerts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forsythe.smartpoultry.presentation.composables.cards.MyCard
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.text.TitleText
import com.forsythe.smartpoultry.presentation.screens.mainActivity.MainActivity
import com.forsythe.smartpoultry.utils.getTimeAgo
import com.ramcosta.composedestinations.annotation.Destination
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination
@Composable
fun AlertScreen(
    //modifier:Modifier
    //navigator: DestinationsNavigator
) {
    val alertsViewModel = hiltViewModel<AlertsViewModel>()
    val listOfAlerts by remember { alertsViewModel.getFlaggedCells() }.collectAsState(initial = emptyList())
    val context = LocalContext.current
    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher?.addCallback(
        LocalLifecycleOwner.current, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //navigate to main activity
                context.startActivity(Intent(context, MainActivity::class.java))
                (context as Activity).finish()
            }

        })

    var showDeleteDialog by remember { mutableStateOf(false) }
    MyInputDialog(
        showDialog = showDeleteDialog,
        title = "Delete Alert",
        onConfirm = {
            alertsViewModel.onDeleteAlert(alertsViewModel.selectedAlertId)
            showDeleteDialog = false
        },
        onDismiss = { showDeleteDialog = false }
    )
    {
        Text(text = "Alert will be deleted from history, confirm to proceed")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Alerts") },
                navigationIcon = {
                    IconButton(onClick = {
                        //navigate back to Main Activity
                        context.startActivity(Intent(context, MainActivity::class.java))
                        (context as Activity).finish()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background,
        ) {
            if (listOfAlerts.isNotEmpty()) {
                Column {

                    MyCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                    ) {
                        Box (
                            modifier = Modifier.fillMaxWidth(),
                           contentAlignment = Alignment.Center
                        ){
                            TitleText(
                                fontSize = 20.sp,
                                text = "${listOfAlerts.filter { alertFull -> !alertFull.attended }.size} alerts not yet attended"
                            )
                        }

                    }

                    LazyColumn() {
                        itemsIndexed(
                            listOfAlerts,
                            key = { _, alert -> alert.alertId }) { index, alert ->
                            // MyVerticalSpacer(height = 5)

                            MyCard (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                            ){
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp)
                                        .animateItemPlacement(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    TitleText(text = (index + 1).toString())
                                    Column {
                                        Text(
                                            text = "Date : ${
                                                SimpleDateFormat("dd MMM, yyyy").format(
                                                    alert.date
                                                )
                                            }"
                                        )
                                        Row {
                                            Text(text = "Block : ${alert.blockNum}")
                                            Text(text = "Cell : ${alert.cellNum}")
                                        }

                                        TitleText(
                                            fontSize = 16.sp,
                                            text = getTimeAgo( alert.date.time)
                                        )
                                    }

                                    if (!alert.attended) {
                                        //Text(text = "Mark attended")
                                        IconButton(onClick = {
                                            alertsViewModel.onMarkAttended(
                                                true,
                                                alert.alertId
                                            )
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.ThumbUp,
                                                contentDescription = "Check"
                                            )
                                        }
                                    }
                                    else {
                                        IconButton(onClick = {
                                            alertsViewModel.onMarkAttended(
                                                false,
                                                alert.alertId
                                            )
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.ThumbUp,
                                                contentDescription = "Check"
                                            )
                                        }
                                    }

                                    IconButton(onClick = {
                                        alertsViewModel.selectedAlertId = alert.alertId
                                        showDeleteDialog = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "delete"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "No cells flagged"
                            )
                        }
                    }
                }
            }

        }
    }
}