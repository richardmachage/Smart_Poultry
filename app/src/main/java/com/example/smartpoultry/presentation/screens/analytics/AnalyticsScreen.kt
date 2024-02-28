package com.example.smartpoultry.presentation.screens.analytics

import android.os.Build
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.presentation.composables.BlocksDropDownMenu
import com.example.smartpoultry.presentation.composables.CellAnalysisGraph
import com.example.smartpoultry.presentation.composables.CellsDropDownMenu
import com.example.smartpoultry.presentation.composables.MonthsDropDownMenu
import com.example.smartpoultry.presentation.composables.MyDatePicker
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.RadioButtonGroup
import com.example.smartpoultry.presentation.composables.YearsDropDownMenu
import com.example.smartpoultry.presentation.uiModels.ChartClass
import com.example.smartpoultry.utils.toGraphDate
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun AnalyticsScreen(
    //modifier : Modifier
) {
    val analyticsViewModel = hiltViewModel<AnalyticsViewModel>()
    val listOfBlocksWithCells by remember { analyticsViewModel.listOfBlocksWithCells }.collectAsState()
    val context = LocalContext.current
    var width by remember {
        mutableFloatStateOf(0.5f)
    }


    //simple logic  for graph of egg collection between dates

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
                // Analysis by cell
                modifier = Modifier
                    /*.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            (0.03 * LocalConfiguration.current.screenWidthDp).dp
                        )
                    )*/
                    .fillMaxWidth()
                    .padding(6.dp),
            ) {
                Text("Trend analysis")

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
                                onItemClick = { blockId, cells ->
                                    listOfCells = cells
                                    analyticsViewModel.selectedBlockId.intValue = blockId
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
                        dateDialogState = rememberMaterialDialogState(),
                        label = "Start Date",
                        positiveButtonOnClick = { pickedDate ->
                            analyticsViewModel.startDate.value = pickedDate
                            analyticsViewModel.plotChart.value = false
                        },
                        negativeButton = {}
                    )


                    MyDatePicker( // end date
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

                if (analyticsViewModel.plotChart.value) {
                    //plot the chart
                    //first get the data...yes but which data??? here there should be a condition
                    //for the kind of data retrieved (Query) based on the type of trend

                    //here, come up with algo to select dataset depending on type of analysis and level of analysis
                    val listOfRecords by remember {
                        if (analyticsViewModel.levelOfAnalysis.value == "Cell") {
                            if (analyticsViewModel.isPastXDaysAnalysis.value) return@remember analyticsViewModel.getCellEggCollectionForPastDays()
                            else if (analyticsViewModel.isCustomRangeAnalysis.value) return@remember analyticsViewModel.getCellCollectionBetweenDates()
                            else return@remember analyticsViewModel.getCellMonthlyRecords()
                        }
                        else if(analyticsViewModel.levelOfAnalysis.value == "Block"){
                            if (analyticsViewModel.isPastXDaysAnalysis.value) return@remember analyticsViewModel.getBlockCollectionsForPastDays()
                            else if (analyticsViewModel.isCustomRangeAnalysis.value) return@remember analyticsViewModel.getBlockEggCollectionBetweenDates()
                            else return@remember analyticsViewModel.getMonthlyBlockCollections()
                        }
                        else{ //meaning the level is just overall
                            if (analyticsViewModel.isPastXDaysAnalysis.value) return@remember analyticsViewModel.getOverallCollectionsForPastDays()
                            else if (analyticsViewModel.isCustomRangeAnalysis.value) return@remember analyticsViewModel.getOverallCollectionBetweenDays()
                            else return@remember analyticsViewModel.getMonthlyOverallCollections()
                        }

                    }.collectAsState(
                            initial = emptyList()
                        )


                    if (listOfRecords.isNotEmpty()) {
                        //plot the chart
                        val itemPlacerCount = 0
                        val turnToChartData = listOfRecords.map { record->
                            when (record){
                                is EggCollection -> ChartClass(
                                    xDateValue = toGraphDate(record.date),
                                    yNumOfEggs = record.eggCount

                                )
                                is DailyEggCollection -> ChartClass(
                                    xDateValue = toGraphDate(record.date),
                                    yNumOfEggs = record.totalEggs
                                )

                                else -> {}
                            }
                        } as List<ChartClass>

                        CellAnalysisGraph(
                            isGraphPlotted = analyticsViewModel.plotChart.value,
                            listOfRecords = turnToChartData.map { it as ChartClass },
                            itemPlacerCount =
                            (turnToChartData.maxOf { chartClass: ChartClass ->  chartClass.yNumOfEggs })+1,
                            startAxisTitle = "Num of Eggs",
                            bottomAxisTitle = "Date",
                        )

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
        }
    }
}