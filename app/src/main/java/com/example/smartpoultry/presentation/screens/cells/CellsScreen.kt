package com.example.smartpoultry.presentation.screens.cells

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
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
    val context = LocalContext.current

    //Edit cell Dialog
    if (cellsViewModel.showDialog.value){
        val cell = cellsViewModel.selectedCell
        cellsViewModel.cellNumText.value = cell.cellNum.toString()
        cellsViewModel.henCountText.value = cell.henCount.toString()
        AlertDialog(
            onDismissRequest = { cellsViewModel.showDialog.value = false },
            title = { Text(text = "Edit Cell")},
            text = {
                   Column {
                       Text(text = "Cell ID : ${cell.cellId}")

                       TextField( //shows cell num
                           value = cellsViewModel.cellNumText.value,
                           onValueChange = {
                                       cellsViewModel.cellNumText.value = it
                           },
                           label = { Text(text = "Cell Number")}
                       )
                       MyVerticalSpacer(height = 5)

                       TextField( //shows hen count
                           value = cellsViewModel.henCountText.value,
                           onValueChange = {
                               cellsViewModel.henCountText.value = it
                           },
                           label = { Text(text = "Hen Count") },

                       )
                   }
            },
            confirmButton = {
                            Button(onClick = {
                                cellsViewModel.updateCellInfo(Cells(
                                    cellId = cell.cellId,
                                    blockId = cell.blockId,
                                    cellNum = cellsViewModel.cellNumText.value.toInt(),
                                    henCount = cellsViewModel.henCountText.value.toInt()
                                    )
                                )
                                Toast.makeText(context,"Cell number ${cell.cellNum} Info updated successfully", Toast.LENGTH_SHORT).show()
                                cellsViewModel.showDialog.value = false
                                //cellsViewModel.clearTextFields()
                            }) {
                                Text(text = "Save")
                            }
            },
            dismissButton = {
                Button(onClick = {
                   // Toast.makeText(context, "Cancel Clicked", Toast.LENGTH_SHORT).show()
                    cellsViewModel.showDialog.value = false
                }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Block : $blockId")
                Text(text = "Number of cells is : ${listOfCells.size}")
            }

            MyVerticalSpacer(height = 3)

            LazyColumn (
                modifier = Modifier.padding(3.dp)
            ){
                itemsIndexed(listOfCells) {_, item ->
                    MyVerticalSpacer(height = 6)
                    Column(
                        modifier = Modifier
                            .clickable {
                                cellsViewModel.setTheSelectedCell(item)
                                cellsViewModel.showDialog.value = true
                            }
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(
                                    (0.03 * LocalConfiguration.current.screenWidthDp).dp
                                )
                            )
                            .padding(6.dp)
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