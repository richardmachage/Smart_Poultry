package com.example.smartpoultry.presentation.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.presentation.composables.MyCardInventory
import com.example.smartpoultry.presentation.composables.MyHorizontalSpacer
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.RecentEggsLineChart
import com.ramcosta.composedestinations.annotation.Destination
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.O)
@Destination
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    //modifier: Modifier
) {

    val context = LocalContext.current
    //viewmodel initialization
    val homeViewModel: HomeViewModel = hiltViewModel()
    val totalBlocks = homeViewModel.totalBlocks.collectAsState()
    val totalCells = homeViewModel.totalCells.collectAsState()
    val dailyEggCollections by homeViewModel.eggCollectionRecords.collectAsState()

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
                            number = totalCells.value.sumOf { cell: Cells -> cell.henCount }
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
                    Card(
                        modifier = Modifier
                            .padding(12.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(10.dp)

                            )
                            .fillMaxWidth()
                        // .height(100.dp)

                    ) {


                        Text(
                            text = "Sacks of Feeds : 16",
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.Start)
                        )
                    }

                }


                NormButton(
                    modifier = Modifier.fillMaxWidth(),
                    onButtonClick = {
                        homeViewModel.onCreateReport(
                            name = "Inventory ${SimpleDateFormat("dd/mm/yyyy").format(System.currentTimeMillis())}",
                            content = "SMART POULTRY INVENTORY " +
                                    "\n"+
                                    "\nDate : ${SimpleDateFormat("dd/mm/yyyy").format(System.currentTimeMillis())}" +
                                    "\n  " +
                                    "\n  " +
                                    "\nTotal Blocks : ${totalBlocks.value.size}" +
                                    "\nTotal Cells: ${totalCells.value.size}" +
                                    "\nTotal Chicken: ${totalCells.value.sumOf { cell: Cells -> cell.henCount }}"
                        )
                        Toast.makeText(
                            context,
                            "File exported successfully, view in downloads",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    btnName = "Export inventory summary as PDF>"
                )
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
                MyVerticalSpacer(height = 10)
                //Create graph
                if (dailyEggCollections.isNotEmpty()) RecentEggsLineChart(dailyEggCollections = dailyEggCollections)

            }

            MyVerticalSpacer(height = 20)

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
                /* Chart(
                     chart = lineChart(),
                     model = ,
                     startAxis = rememberStartAxis(
                         //titleComponent = textComponent(),
                         //title = "Sacks of Feeds"
                     ),
                     bottomAxis = rememberBottomAxis(
                         titleComponent = textComponent(),
                         title = "Date"
                     )
                 )
 */
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPrev() {
    //HomeScreen()
}
