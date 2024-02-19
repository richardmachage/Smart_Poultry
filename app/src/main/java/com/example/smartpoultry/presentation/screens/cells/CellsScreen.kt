package com.example.smartpoultry.presentation.screens.cells

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.presentation.screens.composables.MyVerticalSpacer
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun CellsScreen(
    blockId: Int
) {
    val cellsViewModel = hiltViewModel<CellsViewModel>()
    val listOfCells by remember {
        cellsViewModel.getCellsForBLock(blockId)
    }.collectAsState()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Block : $blockId")
                Text(text = "Number of cells is : ${listOfCells.size}")
            }

            MyVerticalSpacer(height = 3)

            LazyColumn {
                itemsIndexed(listOfCells) { _, item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(
                                    (0.03 * LocalConfiguration.current.screenWidthDp).dp
                                )
                            )
                    ) {
                        Text(text = "Cell Id : ${item.cellId}")
                        Text(text = "Cell number : ${item.cellNum}")
                        Text(text = "Number of Chicken : ${item.henCount}")
                    }

                }
            }
        }
    }
}