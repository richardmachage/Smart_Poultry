package com.example.smartpoultry.presentation.screens.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.presentation.screens.composables.DatePicker
import com.example.smartpoultry.presentation.screens.composables.LineGraph
import com.example.smartpoultry.presentation.screens.composables.MyBottomNavBar
import com.example.smartpoultry.presentation.screens.composables.MyCardInventory
import com.example.smartpoultry.presentation.screens.composables.MyHorizontalSpacer
import com.example.smartpoultry.presentation.screens.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.screens.composables.NormText
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.ramcosta.composedestinations.annotation.Destination

@RequiresApi(Build.VERSION_CODES.O)
@Destination
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    //modifier: Modifier
) {

    //viewmodel initialization
    val homeViewModel: HomeViewModel = hiltViewModel()
    val totalBlocks = homeViewModel.totalBlocks.collectAsState()
    val totalCells = homeViewModel.totalCells.collectAsState()
    val dailyEggCollections = homeViewModel.eggCollectionRecords.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column( //Full Screen column
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            //Text(text = "Smart Poultry")

           // MyVerticalSpacer(height = 20)

            Column(
                //Inventory block

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
                Text(text = "Inventory Status :")
                MyVerticalSpacer(height = 10)

                Column {
                    Row( //inventory cards
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        MyCardInventory(
                            item = "Chicken",
                            number = totalCells.value.sumOf { cell : Cells -> cell.henCount }
                        )

                        MyCardInventory(
                            item = "Blocks",
                            number = totalBlocks.value.size
                        )

                        MyCardInventory(
                            item = "Cells",
                            number = totalCells.value.size
                        )

                    }

                    //The Sacks of feeds Card
                    Card (
                        modifier = Modifier
                            .padding(12.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(10.dp)

                            )
                            .fillMaxWidth()
                           // .height(100.dp)

                    ){
                        Text(
                            text = "Egg collection records, total eggs : ${dailyEggCollections.value.size}",//"Sacks of Feeds : 16",
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.Start)
                        )

                        /*Text(
                            text = "16",
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.Start)
                            // textAlign = TextAlign.Center

                        )*/
                    }

                }


            }

            MyVerticalSpacer(height = 20)

            Column(
                //Alerts Block
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            (0.03 * LocalConfiguration.current.screenWidthDp).dp
                        )
                    )
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text(text = "Alerts:")
                Row(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                    Card {
                        Text(
                            text = "cell 1",

                            modifier = Modifier
                                .padding(3.dp)
                        )
                    }

                    MyHorizontalSpacer(width = 10)

                    Card {
                        Text(
                            text = "cell 12",

                            modifier = Modifier
                                .padding(3.dp)
                        )
                    }

                    MyHorizontalSpacer(width = 10)

                    Card {
                        Text(
                            text = "cell 11",

                            modifier = Modifier
                                .padding(3.dp)
                        )
                    }
                    MyHorizontalSpacer(width = 10)

                    Card {
                        Text(
                            text = "cell 4",

                            modifier = Modifier
                                .padding(3.dp)
                        )
                    }
                    MyHorizontalSpacer(width = 10)

                    Card {
                        Text(
                            text = "cell 3",

                            modifier = Modifier
                                .padding(3.dp)
                        )
                    }
                    MyHorizontalSpacer(width = 10)

                    Card {
                        Text(
                            text = "cell 6",

                            modifier = Modifier
                                .padding(3.dp)
                        )
                    }
                    MyHorizontalSpacer(width = 10)

                    Card {
                        Text(
                            text = "cell 17",

                            modifier = Modifier
                                .padding(3.dp)
                        )
                    }
                    MyHorizontalSpacer(width = 10)

                    Card {
                        Text(
                            text = "cell 16",

                            modifier = Modifier
                                .padding(3.dp)
                        )
                    }
                }
            }

            MyVerticalSpacer(height = 20)

            Column(
                //Recent production trends block
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
                Text(text = "Recent Production Trends:")
/*

                MyVerticalSpacer(height = 10)

                Chart(
                    //line chart
                    chart = lineChart(),
                    //model = homeViewModel.chartEntryModel,
                    chartModelProducer = homeViewModel.chartEntryModelProducer,
                    startAxis = rememberStartAxis(
                       // valueFormatter = homeViewModel.verticalAxisValueFormatter,
                        titleComponent = textComponent(),
                        title = "Eggs Produced"
                    ),
                    bottomAxis = rememberBottomAxis(
                       valueFormatter = homeViewModel.horizontalAxisValueFormatter,
                   //   titleComponent = textComponent(),
                        title = "Date"
                    ),
                )
*/

                MyVerticalSpacer(height = 10)

                LineGraph(
                    chartEntryModel = homeViewModel.chartEntryModelProducer,
                    startAxisTitle = "Eggs produced",
                    bottomAxisTitle = "Date",
                  //  bottomAxisValueFormatter = homeViewModel.horizontalAxisValueFormatter
                )


            }

            /*MyVerticalSpacer(height = 20)

            Column(//Recent feeds Block
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            (0.03 * LocalConfiguration.current.screenWidthDp).dp
                        )
                    )
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text(text = "Recent Feeding Trends:")
                MyVerticalSpacer(height = 10)
                Chart(
                    chart = lineChart(),
                    model = homeViewModel.chartEntryModel,
                    startAxis = rememberStartAxis(
                       //titleComponent = textComponent(),
                        //title = "Sacks of Feeds"
                    ),
                    bottomAxis = rememberBottomAxis(
                       titleComponent = textComponent(),
                        title = "Date"
                    )
                )
            }*/
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPrev() {
  //HomeScreen()
}