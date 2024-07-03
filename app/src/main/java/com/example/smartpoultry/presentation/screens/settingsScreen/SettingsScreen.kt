package com.example.smartpoultry.presentation.screens.settingsScreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.R
import com.example.smartpoultry.domain.permissions.POST_NOTIFICATIONS
import com.example.smartpoultry.domain.permissions.checkIfPermissionGranted
import com.example.smartpoultry.presentation.NavGraphs
import com.example.smartpoultry.presentation.composables.MyBorderedColumn
import com.example.smartpoultry.presentation.composables.MyHorizontalSpacer
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.ToggleButton
import com.example.smartpoultry.presentation.destinations.LogInScreenDestination
import com.example.smartpoultry.utils.CONSUCUTIVE_DAYS_KEY
import com.example.smartpoultry.utils.IS_AUTOMATED_ANALYSIS_KEY
import com.example.smartpoultry.utils.PAST_DAYS_KEY
import com.example.smartpoultry.utils.REPEAT_INTERVAL_KEY
import com.example.smartpoultry.utils.THRESHOLD_RATIO_KEY
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator
) {
    //val navController = rememberNavController()
    val context = LocalContext.current
    val settingsViewModel = hiltViewModel<SettingsViewModel>()

    LaunchedEffect(settingsViewModel.toastMessage.value) {
        val toastMessage = settingsViewModel.toastMessage.value
        if (toastMessage.isNotBlank()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
        settingsViewModel.toastMessage.value = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                Modifier
                    .padding(6.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                MyVerticalSpacer(height = 10)

                //past days
                MyBorderedColumn {
                    Text(text = "Past Days Summary in Home Screen: ")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var showDialog by remember {
                            mutableStateOf(false)
                        }
                        var newPastDays by remember {
                            mutableStateOf(settingsViewModel.pastDays.value)
                        }
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "Consecutive Days to Consider",
                            onConfirm = {
                                settingsViewModel.saveToDataStore(
                                    PAST_DAYS_KEY,
                                    newPastDays
                                )
                                showDialog = false
                                //Log.i(PAST_DAYS_KEY + "on dialog click",newPastDays)
                            },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Default Past Days",
                                keyboardType = KeyboardType.Number,
                                initialText = settingsViewModel.pastDays.collectAsState().value,
                                onValueChange = {
                                    newPastDays = it
                                }
                            )
                        }
                        Text(text = settingsViewModel.pastDays.collectAsState().value)
                        IconButton(onClick = { showDialog = true }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                        }
                    }
                }
                MyVerticalSpacer(height = 10)

                //consucutive days
                MyBorderedColumn {
                    Text(text = "Number of Day for trend analysis (Consecutive days of low production to be considered before flagging a cell?)")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var showDialog by remember {
                            mutableStateOf(false)
                        }
                        var newConsucutiveDays by remember {
                            mutableStateOf(settingsViewModel.consucutiveNumberOfDays.value)
                        }
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "Consecutive Days to Consider",
                            onConfirm = {
                                showDialog = false
                                settingsViewModel.saveToDataStore(
                                    CONSUCUTIVE_DAYS_KEY,
                                    newConsucutiveDays
                                )
                            },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Consecutive Days",
                                keyboardType = KeyboardType.Number,
                                initialText = settingsViewModel.consucutiveNumberOfDays.collectAsState().value,
                                onValueChange = {
                                    newConsucutiveDays = it
                                }
                            )
                        }
                        Text(text = settingsViewModel.consucutiveNumberOfDays.collectAsState().value)
                        IconButton(onClick = { showDialog = true }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                        }
                    }
                }
                MyVerticalSpacer(height = 10)

                //threshold ratio
                MyBorderedColumn {
                    Text(text = "Threshold Ratio for trend analysis (What should be the minimum henCount to EggCount ration in determining poor egg production?)")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var showDialog by remember {
                            mutableStateOf(false)
                        }
                        var newThreshold by remember {
                            mutableStateOf(settingsViewModel.thresholdRatio.value)
                        }
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "Threshold Ratio",
                            onConfirm = {
                                if (validateThresholdInput(newThreshold)) {
                                    showDialog = false
                                    settingsViewModel.saveToDataStore(
                                        THRESHOLD_RATIO_KEY,
                                        newThreshold
                                    )

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Threshold Ratio can only be decimal between 0 and 1",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Threshold Ratio",
                                keyboardType = KeyboardType.Decimal,
                                initialText = settingsViewModel.thresholdRatio.collectAsState().value,
                                onValueChange = {
                                    newThreshold = it
                                }
                            )
                        }
                        Text(text = settingsViewModel.thresholdRatio.collectAsState().value)
                        IconButton(onClick = { showDialog = true }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                        }
                    }
                }
                MyVerticalSpacer(height = 10)

                //repeat analysis period
                MyBorderedColumn {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(text = "Repeat interval for automated analysis")

                        var showDialog by remember { mutableStateOf(false) }
                        var isNotificationPermissionGranted by remember {
                            mutableStateOf(
                                checkIfPermissionGranted(context, POST_NOTIFICATIONS)
                            )
                        }
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestPermission(),
                            onResult = { isGranted ->
                                if (isGranted) {
                                    isNotificationPermissionGranted = true
                                    showDialog = false
                                    settingsViewModel.saveToDataStore(
                                        IS_AUTOMATED_ANALYSIS_KEY,
                                        "1"
                                    )
                                } else {
                                    isNotificationPermissionGranted = false
                                    settingsViewModel.toastMessage.value = "Permission denied..."
                                    showDialog = false
                                }
                            }
                        )
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "Permission Required",
                            onConfirm = {
                                launcher.launch(POST_NOTIFICATIONS)
                                // showDialog = false
                            },
                            onDismiss = {
                                settingsViewModel.toastMessage.value = "Permission denied..."
                                showDialog = false

                            }) {
                            Text(text = "This feature requires use of Notifications.\nAllow notifications Permission to proceed.\nYou can also go to App settings to enable notifications")
                        }


                        ToggleButton(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            isChecked = settingsViewModel.isAutomatedAnalysis.collectAsState().value == "1",
                            onCheckedChange = {
                                if (it) {
                                    if (!isNotificationPermissionGranted) {
                                        showDialog = true
                                    } else {
                                        settingsViewModel.saveToDataStore(
                                            IS_AUTOMATED_ANALYSIS_KEY,
                                            "1"
                                        )
                                    }
                                } else settingsViewModel.saveToDataStore(
                                    IS_AUTOMATED_ANALYSIS_KEY,
                                    "0"
                                )
                            })

                    }
                    //edit part
                    AnimatedVisibility(visible = settingsViewModel.isAutomatedAnalysis.collectAsState().value == "1") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var showDialog by remember {
                                mutableStateOf(false)
                            }
                            var newRepeatInterval by remember {
                                mutableStateOf(settingsViewModel.repeatInterval.value)
                            }
                            MyInputDialog(
                                showDialog = showDialog,
                                title = "Repeat Interval",
                                onConfirm = {
                                    settingsViewModel.saveToDataStore(
                                        REPEAT_INTERVAL_KEY,
                                        newRepeatInterval
                                    )
                                    showDialog = false
                                    //Log.i(PAST_DAYS_KEY + "on dialog click",newPastDays)
                                },
                                onDismiss = { showDialog = false }
                            ) {
                                MyOutlineTextFiled(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "Time in Hours",
                                    keyboardType = KeyboardType.Number,
                                    initialText = settingsViewModel.repeatInterval.collectAsState().value,
                                    onValueChange = {
                                        newRepeatInterval = it
                                    }
                                )
                            }
                            Text(text = "Time in hours: ${settingsViewModel.repeatInterval.collectAsState().value}")
                            IconButton(onClick = { showDialog = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                            }
                        }
                    }

                }
                MyVerticalSpacer(height = 10)

                //confirm log out dialog
                var showLogOutDialog by remember { mutableStateOf(false) }
                MyInputDialog(
                    showDialog = showLogOutDialog,
                    title = "Log Out",
                    onConfirm = {
                        settingsViewModel.viewModelScope.launch {
                            settingsViewModel.onLogOut()
                            showLogOutDialog = false
                            navigator.navigate(LogInScreenDestination) {
                                popUpTo(NavGraphs.root) { inclusive = true }
                            }
                            /*withContext(Dispatchers.Main){
                                (context as Activity).finish()
                            }*/

                        }
                    },
                    onDismiss = {
                        showLogOutDialog = false
                    }
                ) {
                    Text(text = "Are you sure you want to log out?")

                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showLogOutDialog = true
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_logout),
                        contentDescription = "log out"
                    )
                    MyHorizontalSpacer(width = 10)
                    Text(text = "Log Out")
                }
            }
        }
    }

}

fun validateThresholdInput(str: String): Boolean {
    return try {
        val number = str.toDouble()
        number in 0.0..1.0
    } catch (e: NumberFormatException) {
        false // Parsing failed or number is out of range
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevSettings() {
    //SettingsScreen()
}