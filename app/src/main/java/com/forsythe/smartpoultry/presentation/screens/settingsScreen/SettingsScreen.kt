package com.forsythe.smartpoultry.presentation.screens.settingsScreen

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.domain.permissions.POST_NOTIFICATIONS
import com.forsythe.smartpoultry.domain.permissions.checkIfPermissionGranted
import com.forsythe.smartpoultry.presentation.NavGraphs
import com.forsythe.smartpoultry.presentation.composables.buttons.MyTextButton
import com.forsythe.smartpoultry.presentation.composables.buttons.ToggleButton
import com.forsythe.smartpoultry.presentation.composables.cards.MyCard
import com.forsythe.smartpoultry.presentation.composables.dialogs.InfoDialog
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.spacers.MyHorizontalSpacer
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.text.TitleText
import com.forsythe.smartpoultry.presentation.composables.textInputFields.MyOutlineTextFiled
import com.forsythe.smartpoultry.presentation.destinations.LogInScreenDestination
import com.forsythe.smartpoultry.utils.ABOUT_US_LINK
import com.forsythe.smartpoultry.utils.CONSUCUTIVE_DAYS_KEY
import com.forsythe.smartpoultry.utils.CONTACT_US
import com.forsythe.smartpoultry.utils.IS_AUTOMATED_ANALYSIS_KEY
import com.forsythe.smartpoultry.utils.PAST_DAYS_KEY
import com.forsythe.smartpoultry.utils.PRIVACY_POLICY_LINK
import com.forsythe.smartpoultry.utils.REPEAT_INTERVAL_KEY
import com.forsythe.smartpoultry.utils.THRESHOLD_RATIO_KEY
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
                    .padding(start = 16.dp, end = 16.dp)
                    .verticalScroll(rememberScrollState()),

            ) {
                MyVerticalSpacer(height = 10)

                //past days
                MyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween

                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = R.string.past_days_summarry),//"Past Days Summary in Home Screen",
                                fontWeight = FontWeight.Bold
                            )
                            var showPastDayInfo by remember{ mutableStateOf(false) }
                            InfoDialog(
                                showDialog = showPastDayInfo,
                                title = stringResource(id = R.string.past_days_summarry),
                                message = stringResource(id = R.string.past_days_summarry_description),
                                onConfirm = { showPastDayInfo = false })
                            IconButton(onClick = { showPastDayInfo = true }) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
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
                                title = stringResource(id = R.string.past_days_summarry),//"Consecutive Days to Consider",
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

                            TitleText(
                                modifier = Modifier.padding(start = 10.dp),
                                text = settingsViewModel.pastDays.collectAsState().value)

                            IconButton(onClick = { showDialog = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                            }
                        }
                    }
                }
                MyVerticalSpacer(height = 10)

                //consucutive days
                MyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = R.string.number_of_days_for_trend_analysis),//"Number of Days for trend analysis ",
                                        fontWeight = FontWeight.Bold

                            ) //(Consecutive days of low production to be considered before flagging a cell?)")
                            var showNumberOfDaysAnalysisInfo by remember { mutableStateOf(false) }
                            InfoDialog(
                                showDialog = showNumberOfDaysAnalysisInfo,
                                title = stringResource(id = R.string.number_of_days_for_trend_analysis),
                                message = stringResource(id = R.string.number_of_days_for_trend_analysis_description),
                                onConfirm = { 
                                    showNumberOfDaysAnalysisInfo = false
                                })
                            IconButton(onClick = { showNumberOfDaysAnalysisInfo = true }) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            //.padding(6.dp),
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
                                title = stringResource(id = R.string.number_of_days_for_trend_analysis),//"Consecutive Days to Consider",
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
                            TitleText(
                                modifier = Modifier.padding(start = 10.dp),
                                text = settingsViewModel.consucutiveNumberOfDays.collectAsState().value)
                            IconButton(onClick = { showDialog = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                            }
                        }
                    }
                }
                MyVerticalSpacer(height = 10)

                //threshold ratio
                MyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = R.string.threshold_ratio),//"Threshold Ratio for trend analysis",
                                fontWeight = FontWeight.Bold

                            )//(What should be the minimum henCount to EggCount ration in determining poor egg production?)")
                            var thresholdRatioInfo by remember { mutableStateOf(false) }
                            InfoDialog(
                                showDialog = thresholdRatioInfo,
                                title = stringResource(id = R.string.threshold_ratio),
                                message = stringResource(id = R.string.threshold_ratio_description),
                                onConfirm = { thresholdRatioInfo = false })
                            IconButton(onClick = { thresholdRatioInfo = true }) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            //.padding(6.dp),
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
                                title = stringResource(id = R.string.threshold_ratio),//"Threshold Ratio",
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
                            TitleText(
                                modifier = Modifier.padding(start = 10.dp),
                                text = settingsViewModel.thresholdRatio.collectAsState().value)

                            IconButton(onClick = { showDialog = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                            }
                        }
                    }
                }
                MyVerticalSpacer(height = 10)

                //repeat analysis period
                MyCard (
                    modifier = Modifier.fillMaxWidth()
                ){
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            text = stringResource(id = R.string.repeat_interval),//"Repeat interval",
                            fontWeight = FontWeight.Bold

                        )

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

                        //Toggle button
                        Row {
                            ToggleButton(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                isChecked = settingsViewModel.isAutomatedAnalysis.collectAsState().value == "1",
                                onCheckedChange = {
                                    if (it) {
                                        if (isNotificationPermissionGranted) {
                                            settingsViewModel.saveToDataStore(
                                                IS_AUTOMATED_ANALYSIS_KEY,
                                                "1"
                                            )
                                            settingsViewModel.setWorker()
                                        } else {
                                            showDialog = true
                                        }
                                    } else {
                                        settingsViewModel.saveToDataStore(
                                            IS_AUTOMATED_ANALYSIS_KEY,
                                            "0"
                                        )
                                        settingsViewModel.cancelWorker()
                                    }
                                }
                            )

                            MyHorizontalSpacer(width = 5)
                            
//                            var thresholdRatioInfo by remember { mutableStateOf(false) }
                            var showIntervalInfoDialog by remember { mutableStateOf(false)}
                            InfoDialog(
                                showDialog = showIntervalInfoDialog,
                                title = stringResource(id = R.string.repeat_interval),
                                message = stringResource(id = R.string.repeat_interval_description),
                                onConfirm = { showIntervalInfoDialog = false })
                            IconButton(onClick = { showIntervalInfoDialog = true }) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            }
                        }

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
                                title = stringResource(id = R.string.repeat_interval),//"Repeat Interval",
                                onConfirm = {
                                    settingsViewModel.saveToDataStore(
                                        REPEAT_INTERVAL_KEY,
                                        newRepeatInterval
                                    )
                                    settingsViewModel.setWorker()
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
                            TitleText(
                                modifier = Modifier.padding(start = 10.dp),
                                text = settingsViewModel.repeatInterval.collectAsState().value)
                            Text(text = stringResource(id = R.string.time_in_hours))//"Time in hours")

                            IconButton(onClick = { showDialog = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                            }
                        }
                    }

                }
            }
                MyVerticalSpacer(height = 10)

                //Links
                MyCard (
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column {

                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            MyTextButton(//Privacy policy
                                onButtonClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_LINK))
                                    context.startActivity(intent)
                                },
                                btnText = stringResource(id = R.string.privacy_policy)
                            )

                            //Give feedBack dialog
                            var showFeedbackDialog by remember{ mutableStateOf(false) }
                            MyInputDialog(
                                showDialog = showFeedbackDialog,
                                title = stringResource(id = R.string.give_feedback),
                                confirmBtnName = stringResource(id = R.string.send_btn),
                                onConfirm = {
                                    settingsViewModel.onSendFeedback()
                                    showFeedbackDialog = false
                                },
                                onDismiss = {
                                    showFeedbackDialog = false
                                }
                            )
                            {
                                var text by remember { mutableStateOf("") }
                                MyOutlineTextFiled(
                                    label = stringResource(id = R.string.give_feedback),
                                    keyboardType = KeyboardType.Text,
                                    initialText = text,
                                    onValueChange = {
                                        text = it
                                    }
                                )
                            }
                            MyTextButton(//FeedBack
                                onButtonClick = { showFeedbackDialog = true },
                                btnText = stringResource(id = R.string.give_feedback)
                            )
                        }
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ){

                            MyTextButton(//Contact us
                                onButtonClick = {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(CONTACT_US)
                                        )
                                    )
                                },
                                btnText = stringResource(id = R.string.contact_us)
                            )
                            MyTextButton(//About info
                                onButtonClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ABOUT_US_LINK))
                                    context.startActivity(intent)
                                },
                                btnText = stringResource(id = R.string.about_info)
                            )
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
                        }
                    },
                    onDismiss = {
                        showLogOutDialog = false
                    }
                ) {
                    Text(text = "Are you sure you want to log out?")

                }
                OutlinedButton(
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
