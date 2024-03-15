package com.example.smartpoultry.presentation.screens.home

import android.os.Build
import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.presentation.composables.MyCardInventory
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.RecentEggsLineChart
import com.example.smartpoultry.presentation.screens.settingsScreen.PAST_DAYS_KEY
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
    //val dailyEggCollections by homeViewModel.eggCollectionRecords.collectAsState()
    val userRole by homeViewModel.userRole.collectAsState()

    val pastDaysState = remember { homeViewModel.dataStore.readData(PAST_DAYS_KEY) }.collectAsState(initial = "0")
    val pastDays = pastDaysState.value.toIntOrNull() ?: 0

    val dailyEggsForPastDays: State<List<DailyEggCollection>> = produceState(initialValue = emptyList(), key1 = pastDays) {
        if (pastDays > 0) {
            homeViewModel.getOverallCollectionsForPastDays(pastDays).collect {
                value = it
            }
        }
    }

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

            //Type of role card
            Card(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(10.dp)

                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Past days fom datastore: $pastDays and daily records: ${dailyEggsForPastDays.value.size}" ,//"Logged in as: $userRole",
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.Start)
                )
            }

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

                NormButton(
                    modifier = Modifier.fillMaxWidth(),
                    onButtonClick = {
                        val reportType = "Farm Inventory Status"
                        homeViewModel.onCreateReport(
                            name = "$reportType ${SimpleDateFormat("dd/MMM/yyyy").format(System.currentTimeMillis())}",
                            content =
                                    "\nTotal Blocks : ${totalBlocks.value.size}" +
                                    "\nTotal Cells: ${totalCells.value.size}" +
                                    "\nTotal Chicken: ${totalCells.value.sumOf { cell: Cells -> cell.henCount }}",
                            reportType = reportType
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
               if (dailyEggsForPastDays.value.isNotEmpty()) RecentEggsLineChart(dailyEggCollections = dailyEggsForPastDays.value)
               //if (dailyEggForPastDays.isNotEmpty()) RecentEggsLineChart(dailyEggCollections = dailyEggForPastDays)

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
