package com.forsythe.smartpoultry.presentation.screens.analytics

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.data.dataModels.DailyEggCollection
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollection
import com.forsythe.smartpoultry.domain.permissions.POST_NOTIFICATIONS
import com.forsythe.smartpoultry.domain.permissions.checkIfPermissionGranted
import com.forsythe.smartpoultry.presentation.composables.ads.rewardedAdSmartPoultry
import com.forsythe.smartpoultry.presentation.composables.buttons.MyOutlineButton
import com.forsythe.smartpoultry.presentation.composables.buttons.NormButton
import com.forsythe.smartpoultry.presentation.composables.buttons.RadioButtonGroup
import com.forsythe.smartpoultry.presentation.composables.dialogs.ConfirmDialog
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.dropDownMenus.BlocksDropDownMenu
import com.forsythe.smartpoultry.presentation.composables.dropDownMenus.CellsDropDownMenu
import com.forsythe.smartpoultry.presentation.composables.dropDownMenus.MonthsDropDownMenu
import com.forsythe.smartpoultry.presentation.composables.dropDownMenus.YearsDropDownMenu
import com.forsythe.smartpoultry.presentation.composables.others.MyDatePicker
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.destinations.ViewRecordsScreenDestination
import com.forsythe.smartpoultry.presentation.uiModels.ChartClass
import com.forsythe.smartpoultry.utils.EXPORT_TO_PDF_REWARDED_AD_ID
import com.forsythe.smartpoultry.utils.localDateToJavaDate
import com.forsythe.smartpoultry.utils.toGraphDate
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun AnalyticsScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val analyticsViewModel = hiltViewModel<AnalyticsViewModel>()
    val listOfBlocksWithCells by remember { analyticsViewModel.listOfBlocksWithCells }.collectAsState()

    var width by remember {
        mutableFloatStateOf(0.5f)
    }

    var reportType by remember { mutableStateOf("") }

    LaunchedEffect(analyticsViewModel.toastMessage.value) {
        val toastMessage = analyticsViewModel.toastMessage.value
        if (toastMessage.isNotBlank()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
        analyticsViewModel.toastMessage.value = ""

    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                // level of Analysis by cell
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
            ) {

                RadioButtonGroup(
                    title = "Select level of analysis:",
                    listOfOptions = listOf("Cell", "Block", "Overall"),
                    onOptionSelect = { selectedOption ->
                        analyticsViewModel.levelOfAnalysis.value = selectedOption
                        if (selectedOption == "Block") width = 1f
                        if (selectedOption == "Cell") width = 0.5f
                    }
                )

                MyVerticalSpacer(height = 8)
                RadioButtonGroup(
                    title = "Select type of analysis:",
                    listOfOptions = listOf("Past\nX Days", "Custom\nRange", "Monthly"),
                    onOptionSelect = { selectedOption ->
                        when (selectedOption) {
                            "Custom\nRange" -> {
                                analyticsViewModel.isCustomRangeAnalysis.value = true
                                analyticsViewModel.isPastXDaysAnalysis.value = false
                                analyticsViewModel.isMonthlyAnalysis.value = false
                            }

                            "Past\nX Days" -> {
                                analyticsViewModel.isCustomRangeAnalysis.value = false
                                analyticsViewModel.isPastXDaysAnalysis.value = true
                                analyticsViewModel.isMonthlyAnalysis.value = false
                            }

                            "Monthly" -> {
                                analyticsViewModel.isCustomRangeAnalysis.value = false
                                analyticsViewModel.isPastXDaysAnalysis.value = false
                                analyticsViewModel.isMonthlyAnalysis.value = true
                            }
                        }
                        analyticsViewModel.plotChart.value = false
                    },
                )
                MyVerticalSpacer(height = 8)


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    var listOfCells by remember { mutableStateOf(emptyList<Cells>()) }

                    if (listOfBlocksWithCells.isNotEmpty()) {

                        if (analyticsViewModel.levelOfAnalysis.value != "Overall") {
                            BlocksDropDownMenu(
                                listOfItems = listOfBlocksWithCells,
                                onItemClick = { blockId, blockNum, cells ->
                                    listOfCells = cells
                                    analyticsViewModel.selectedBlockId.intValue = blockId
                                    analyticsViewModel.selectedBlockNum.intValue = blockNum
                                    analyticsViewModel.plotChart.value = false
                                },
                                width = width,
                            )

                            //toggle to show cells drop down list only when analysis level is by cell
                            if (analyticsViewModel.levelOfAnalysis.value == "Cell") {
                                CellsDropDownMenu(
                                    listOfItems = listOfCells,
                                    onItemClick = { cell ->
                                        analyticsViewModel.selectedCellID.intValue = cell.cellId
                                        analyticsViewModel.selectedCellNum.intValue = cell.cellNum
                                        analyticsViewModel.plotChart.value = false
                                    }
                                )
                            }
                        }
                    }
                }

                if (analyticsViewModel.isPastXDaysAnalysis.value) {
                    //MyVerticalSpacer(height = 3)
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = "Analyze the trends for a given number of past days"
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        value = analyticsViewModel.pastDays.value,
                        onValueChange = { newText ->
                            analyticsViewModel.pastDays.value = newText.trim()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Number of Past Days") },
                        maxLines = 1,
                    )

                }

                if (analyticsViewModel.isMonthlyAnalysis.value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        YearsDropDownMenu(
                            onItemClick = { year ->
                                analyticsViewModel.selectedYear.value = year
                                analyticsViewModel.plotChart.value = false
                            }
                        )

                        MonthsDropDownMenu(
                            onItemClick = { month ->
                                analyticsViewModel.selectedMonth.value = month
                                analyticsViewModel.plotChart.value = false
                            }
                        )
                    }
                }

                if (analyticsViewModel.isCustomRangeAnalysis.value) {
                    MyDatePicker(// start date
                        modifier = Modifier.fillMaxWidth(),
                        dateDialogState = rememberMaterialDialogState(),
                        label = "Start Date",
                        positiveButtonOnClick = { pickedDate ->
                            analyticsViewModel.startDate.value = pickedDate
                            analyticsViewModel.plotChart.value = false
                        },
                        negativeButton = {}
                    )


                    MyDatePicker( // end date
                        modifier = Modifier.fillMaxWidth(),
                        dateDialogState = rememberMaterialDialogState(),
                        label = "End Date",
                        positiveButtonOnClick = { pickedDate ->
                            analyticsViewModel.endDate.value = pickedDate
                            analyticsViewModel.plotChart.value = false
                        },
                        negativeButton = {}
                    )
                }

                val buttonText by remember { mutableStateOf("Plot graph") }
                NormButton(
                    onButtonClick = {
                        analyticsViewModel.plotChart.value = !analyticsViewModel.plotChart.value
                    },
                    btnName = buttonText,
                    modifier = Modifier.fillMaxWidth()
                )

                MyVerticalSpacer(height = 10)

                //Plot charts
                if (analyticsViewModel.plotChart.value) {
                    //plot the chart
                    //first get the data...yes but which data??? here there should be a condition
                    //for the kind of data retrieved (Query) based on the type of trend

                    //here, come up with algo to select dataset depending on type of analysis and level of analysis
                    val listOfRecords by remember {
                        if (analyticsViewModel.levelOfAnalysis.value == "Cell") {
                            if (analyticsViewModel.isPastXDaysAnalysis.value) {
                                reportType =
                                    "Collections of cell ${analyticsViewModel.selectedCellNum.intValue} in Block ${analyticsViewModel.selectedBlockNum.intValue} for duration of Past ${analyticsViewModel.pastDays.value} days"
                                return@remember analyticsViewModel.getCellEggCollectionForPastDays()
                            } else if (analyticsViewModel.isCustomRangeAnalysis.value) {
                                reportType = "Collections of " +
                                        "cell ${analyticsViewModel.selectedCellNum.intValue} in " +
                                        "Block ${analyticsViewModel.selectedBlockNum.intValue} " +
                                        "from ${
                                            SimpleDateFormat("dd MMM,yyyy").format(
                                                localDateToJavaDate(analyticsViewModel.startDate.value)
                                            )
                                        } " +
                                        "to ${
                                            SimpleDateFormat("dd MMM,yyyy").format(
                                                localDateToJavaDate(analyticsViewModel.endDate.value)
                                            )
                                        } "
                                return@remember analyticsViewModel.getCellCollectionBetweenDates()
                            } else {  //meaning analysis is just monthly
                                reportType = "Collections of " +
                                        "cell ${analyticsViewModel.selectedCellNum.intValue} in " +
                                        "Block ${analyticsViewModel.selectedCellNum.intValue} " +
                                        "for ${analyticsViewModel.selectedMonth.value}," +
                                        "${analyticsViewModel.selectedYear}"

                                return@remember analyticsViewModel.getCellMonthlyRecords()
                            }
                        } else if (analyticsViewModel.levelOfAnalysis.value == "Block") {
                            if (analyticsViewModel.isPastXDaysAnalysis.value) {
                                reportType =
                                    "Block ${analyticsViewModel.selectedBlockNum.intValue} " +
                                            "for duration of Past ${analyticsViewModel.pastDays.value} days"
                                return@remember analyticsViewModel.getBlockCollectionsForPastDays()
                            } else if (analyticsViewModel.isCustomRangeAnalysis.value) {
                                reportType = "Collections of " +
                                        "Block ${analyticsViewModel.selectedBlockNum.intValue} " +
                                        "from ${
                                            SimpleDateFormat("dd MMM,yyyy").format(
                                                localDateToJavaDate(analyticsViewModel.startDate.value)
                                            )
                                        } " +
                                        "to ${
                                            SimpleDateFormat("dd MMM,yyyy").format(
                                                localDateToJavaDate(analyticsViewModel.endDate.value)
                                            )
                                        } "
                                return@remember analyticsViewModel.getBlockEggCollectionBetweenDates()
                            } else { //monthly
                                reportType = "Collections of " +
                                        "Block ${analyticsViewModel.selectedBlockNum.intValue} " +
                                        "for ${analyticsViewModel.selectedMonth.value}, " +
                                        "${analyticsViewModel.selectedYear}"
                                return@remember analyticsViewModel.getMonthlyBlockCollections()
                            }
                        } else { //meaning the level is just overall
                            if (analyticsViewModel.isPastXDaysAnalysis.value) {
                                reportType = "Total Egg Collections " +
                                        "for duration of Past ${analyticsViewModel.pastDays.value} days"
                                return@remember analyticsViewModel.getOverallCollectionsForPastDays()
                            } else if (analyticsViewModel.isCustomRangeAnalysis.value) {
                                reportType = "Total Egg Collections " +
                                        "from ${
                                            SimpleDateFormat("dd MMM,yyyy").format(
                                                localDateToJavaDate(analyticsViewModel.startDate.value)
                                            )
                                        } " +
                                        "to ${
                                            SimpleDateFormat("dd MMM,yyyy").format(
                                                localDateToJavaDate(analyticsViewModel.endDate.value)
                                            )
                                        } "
                                return@remember analyticsViewModel.getOverallCollectionBetweenDays()
                            } else { //monthly
                                reportType = "Total Egg Collections " +
                                        "for ${analyticsViewModel.selectedMonth.value}, " +
                                        "${analyticsViewModel.selectedYear}"
                                return@remember analyticsViewModel.getMonthlyOverallCollections()
                            }
                        }

                    }.collectAsState(
                        initial = emptyList()
                    )


                    if (listOfRecords.isNotEmpty()) {
                        //plot the chart
                        val itemPlacerCount = 0
                        val turnToChartData = listOfRecords.map { record ->
                            when (record) {
                                is EggCollection -> ChartClass(
                                    xDateValue = toGraphDate(record.date),//as java.sql.Date),
                                    yNumOfEggs = record.eggCount,
                                    numOfChicken = record.henCount

                                )

                                is DailyEggCollection -> ChartClass(
                                    xDateValue = toGraphDate(record.date),
                                    yNumOfEggs = record.totalEggs,
                                )

                                else -> {}
                            }
                        } as List<ChartClass>


                        if (analyticsViewModel.levelOfAnalysis.value == "Cell") {
                            CellAnalysisGraph(
                                isGraphPlotted = analyticsViewModel.plotChart.value,
                                myListOfRecords = turnToChartData.map { it as ChartClass },
                                itemPlacerCount = (turnToChartData.maxOf { chartClass: ChartClass -> chartClass.yNumOfEggs }) + 1,
                                startAxisTitle = "Num of eggs/chicken",
                                bottomAxisTitle = "Date",
                                reportType = reportType
                            )
                        } else {
                            AnalysisGraph(
                                isGraphPlotted = analyticsViewModel.plotChart.value,
                                myListOfRecords = turnToChartData.map { it as ChartClass },
                                itemPlacerCount =
                                (turnToChartData.maxOf { chartClass: ChartClass -> chartClass.yNumOfEggs }) + 1,
                                startAxisTitle = "Num of Eggs",
                                bottomAxisTitle = "Date",
                                reportType = reportType
                            )
                        }


                    } else {
                        // analyticsViewModel.plotChart.value = false
                        Text(text = "Not data retrieved for that period")
                        MyVerticalSpacer(height = 10)
                        Text(text = "Hint:")
                        Text(text = "-Check if you have selected correct Block and Cell")
                        Text(text = "-Check if you have selected correct dates")
                    }

                }//else  buttonText = "Refresh Charts"


            }

            MyVerticalSpacer(height = 10)


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
                        analyticsViewModel.fireWorker(context)
                    } else {
                        isNotificationPermissionGranted = false
                        showDialog = false
                        analyticsViewModel.toastMessage.value =
                            "Permission denied...Go to settings and enable notifications permission"
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
                    analyticsViewModel.toastMessage.value = "Permission denied..."
                    showDialog = false

                }) {
                Text(text = "This feature requires use of Notifications.\nAllow notifications Permission to proceed.")
            }

            //show ad confirm dialog
            var showAdDialog by remember { mutableStateOf(false) }
            ConfirmDialog(
                showDialog = showAdDialog,
                title = stringResource(R.string.automated_analysis),
                message = stringResource(R.string.automated_analysis_is_premium),
                onDismiss = {
                    showAdDialog = false
                },
                onConfirm = {
                    //show the ad and perform analysis on reward attained
                    rewardedAdSmartPoultry(
                        context = context,
                        adUnitId = EXPORT_TO_PDF_REWARDED_AD_ID,
                        coroutineScope = analyticsViewModel.viewModelScope,
                        onRewardEarned = {
                            // check is notification  permission is allowed or not
                            if (isNotificationPermissionGranted) {
                                analyticsViewModel.fireWorker(context)
                            } else {
                                showDialog = true
                            }
                        },
                        onFailedToLoadAd = {
                            Toast.makeText(context,"failed to load ad",Toast.LENGTH_SHORT).show()
                            showAdDialog = false
                        },
                        onDismiss = {
                            showAdDialog = false
                        }
                    )
                },

            )
            MyOutlineButton(
                modifier = Modifier.fillMaxWidth(),
                onButtonClick = {
                    showAdDialog = true
                },
                btnName = "Perform Automated Analysis",
                trailingIcon = if (
                //todo check if premium subscription
                    true) {
                    Icons.Default.Star
                } else {
                    null
                }

            )

            MyOutlineButton(
                modifier = Modifier.fillMaxWidth(),
                onButtonClick = {
                    navigator.navigate(ViewRecordsScreenDestination)
                }, btnName = "View All records >>>"
            )
        }
    }
}