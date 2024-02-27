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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.presentation.screens.composables.BlocksDropDownMenu
import com.example.smartpoultry.presentation.screens.composables.CellAnalysisGraph
import com.example.smartpoultry.presentation.screens.composables.CellsDropDownMenu
import com.example.smartpoultry.presentation.screens.composables.MonthsDropDownMenu
import com.example.smartpoultry.presentation.screens.composables.MyDatePicker
import com.example.smartpoultry.presentation.screens.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.screens.composables.NormButton
import com.example.smartpoultry.presentation.screens.composables.RadioButtonGroup
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
        mutableFloatStateOf(0.4f)
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
                        if (selectedOption == "Cell") width = 0.4f
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
                                onItemClick = {
                                    listOfCells = it
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
                    MyVerticalSpacer(height = 5)

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        text = "Analyze the trends for a given number of past days"
                    )

                    var text by remember {
                        mutableStateOf("")
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Number of Past Days") },
                        maxLines = 1,
                    )

                }

                if (analyticsViewModel.isMonthlyAnalysis.value) {
                    MonthsDropDownMenu(
                        onItemClick = { month ->
                            analyticsViewModel.selectedMonth.value = month
                        }
                    )
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

                var buttonText by remember { mutableStateOf("Plot graph") }
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
                    val listOfRecords by remember { analyticsViewModel.getCellCollectionBetweenDates() }.collectAsState(
                        initial = emptyList()
                    )



                    if (listOfRecords.isNotEmpty()) {
                        //plot the chart
                        val turnToChartData = listOfRecords.map {
                            ChartClass(
                                xDateValue = toGraphDate(it.date),
                                yNumOfEggs = it.eggCount
                            )
                        }

                        CellAnalysisGraph(
                            isGraphPlotted = analyticsViewModel.plotChart.value,
                            listOfRecords = turnToChartData,
                            itemPlacerCount = 6,
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