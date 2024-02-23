package com.example.smartpoultry.presentation.screens.analytics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.presentation.screens.composables.BlocksDropDownMenu
import com.example.smartpoultry.presentation.screens.composables.CellsDropDownMenu
import com.ramcosta.composedestinations.annotation.Destination

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun AnalyticsScreen(
    //modifier : Modifier
) {
    val analyticsViewModel = hiltViewModel<AnalyticsViewModel>()
    val listOfBlocksWithCells by remember { analyticsViewModel.listOfBlocksWithCells }.collectAsState()


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

            Column( // Analysis by cell
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

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    var listOfCells by remember { mutableStateOf(emptyList<Cells>()) }
                    if (listOfBlocksWithCells.isNotEmpty()) {
                        BlocksDropDownMenu(
                            listOfItems = listOfBlocksWithCells,
                            onItemClick = {
                                listOfCells = it
                            }
                        )

                        CellsDropDownMenu(listOfItems = listOfCells, onItemClick = {} )
                    }
                }
            }

        }
    }
}