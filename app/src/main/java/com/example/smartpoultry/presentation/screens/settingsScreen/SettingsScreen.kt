package com.example.smartpoultry.presentation.screens.settingsScreen

import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.NavGraphs
import com.example.smartpoultry.destinations.LogInScreenDestination
import com.example.smartpoultry.presentation.composables.MyBorderedColumn
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.ToggleButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator
) {
    //val navController = rememberNavController()
    val context = LocalContext.current
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val pastDays =
        remember { settingsViewModel.myDataStore.readData(PAST_DAYS_KEY) }.collectAsState(initial = "0")
    val consucutiveDays =
        remember { settingsViewModel.myDataStore.readData(CONSUCUTIVE_DAYS_KEY) }.collectAsState(
            initial = "0"
        )
    val thresholdRatio =
        remember { settingsViewModel.myDataStore.readData(THRESHOLD_RATIO_KEY) }.collectAsState(
            initial = "0"
        )
    val repeatInterval =
        remember { settingsViewModel.myDataStore.readData(REPEAT_INTERVAL_KEY) }.collectAsState(
            initial = "0"
        )
    val isAutomatedAnalysis = remember {
        settingsViewModel.myDataStore.readData(IS_AUTOMATED_ANALYSIS_KEY)
    }.collectAsState(initial = "0")

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
                            mutableStateOf(pastDays.value)
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
                                initialText = pastDays.value,
                                onValueChange = {
                                    newPastDays = it
                                }
                            )
                        }
                        Text(text = pastDays.value)
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
                            mutableStateOf(consucutiveDays.value)
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
                                initialText = consucutiveDays.value,
                                onValueChange = {
                                    newConsucutiveDays = it
                                }
                            )
                        }
                        Text(text = consucutiveDays.value)
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
                            mutableStateOf(thresholdRatio.value)
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
                                initialText = thresholdRatio.value,
                                onValueChange = {
                                    newThreshold = it
                                }
                            )
                        }
                        Text(text = thresholdRatio.value)
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
                        ToggleButton(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            isChecked = isAutomatedAnalysis.value == "1",
                            onCheckedChange = {
                                if (it) settingsViewModel.saveToDataStore(
                                    IS_AUTOMATED_ANALYSIS_KEY,
                                    "1"
                                ) else settingsViewModel.saveToDataStore(
                                    IS_AUTOMATED_ANALYSIS_KEY,
                                    "0"
                                )
                            })

                    }
                    //edit part
                    if (isAutomatedAnalysis.value == "1"){
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
                                mutableStateOf(repeatInterval.value)
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
                                    initialText = repeatInterval.value,
                                    onValueChange = {
                                        newRepeatInterval = it
                                    }
                                )
                            }
                            Text(text ="Time in hours: ${repeatInterval.value}")
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
                        settingsViewModel.onLogOut()
                        showLogOutDialog = false
                        navigator.navigate(LogInScreenDestination) {
                            popUpTo(NavGraphs.root) { inclusive = true }
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