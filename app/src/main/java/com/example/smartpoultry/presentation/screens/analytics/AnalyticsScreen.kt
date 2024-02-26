package com.example.smartpoultry.presentation.screens.analytics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.presentation.screens.composables.BlocksDropDownMenu
import com.example.smartpoultry.presentation.screens.composables.CellAnalysisGraph
import com.example.smartpoultry.presentation.screens.composables.CellsDropDownMenu
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
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            (0.03 * LocalConfiguration.current.screenWidthDp).dp
                        )
                    )
                    .fillMaxWidth()
                    .padding(6.dp),
            ) {
                Text("Trend analysis by cell:")

                RadioButtonGroup(
                    listOfOptions = listOf("General Analysis", "Range Analysis"),
                    onOptionSelect = { selectedOption ->
                        analyticsViewModel.isRangeAnalysis.value =
                            (selectedOption == "Range Analysis")
                    }
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    var listOfCells by remember { mutableStateOf(emptyList<Cells>()) }
                    if (listOfBlocksWithCells.isNotEmpty()) {
                        BlocksDropDownMenu(
                            listOfItems = listOfBlocksWithCells,
                            onItemClick = {
                                listOfCells = it
                                analyticsViewModel.plotChart.value = false

                            }
                        )

                        CellsDropDownMenu(
                            listOfItems = listOfCells,
                            onItemClick = { cell ->
                                analyticsViewModel.selectedCellID.intValue = cell.cellId
                                analyticsViewModel.plotChart.value = false

                            }
                        )
                    }
                }

                MyDatePicker(// start date
                    dateDialogState = rememberMaterialDialogState(),
                    label = "Start Date",
                    positiveButtonOnClick = { pickedDate ->
                        analyticsViewModel.startDate.value = pickedDate
                        analyticsViewModel.plotChart.value = false
                    },
                    negativeButton = {}
                )

                if (analyticsViewModel.isRangeAnalysis.value) {
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

                        if (!analyticsViewModel.plotChart.value) {
                            analyticsViewModel.plotChart.value = true
                        } else {
                            analyticsViewModel.plotChart.value = false
                        }
                    },
                    btnName = buttonText,
                    modifier = Modifier.fillMaxWidth()
                )

                MyVerticalSpacer(height = 10)

                if (analyticsViewModel.plotChart.value) {
                    //plot the chart
                    //first get the data
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