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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.presentation.composables.MyCard
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.uiModels.BlockParse
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CellsScreen(
    navigator: DestinationsNavigator,
    //blockId: Int
    block : BlockParse
) {
    val cellsViewModel = hiltViewModel<CellsViewModel>()
    val userRole by cellsViewModel.userRole.collectAsState()
    val listOfCells by remember {
        cellsViewModel.getCellsForBLock(block.blockId)
    }.collectAsState()
    val context = LocalContext.current

    //Edit cell Dialog
    if (cellsViewModel.showDialog.value) {
        val cell = cellsViewModel.selectedCell
        cellsViewModel.cellNumText.value = cell.cellNum.toString()
        cellsViewModel.henCountText.value = cell.henCount.toString()
        AlertDialog(
            onDismissRequest = { cellsViewModel.showDialog.value = false },
            title = { Text(text = "Edit Cell") },
            text = {
                Column {
                    Text(text = "Cell ID : ${cell.cellId}")

                    MyOutlineTextFiled(
                        label = "Cell Number",
                        keyboardType = KeyboardType.Number ,
                        initialText = cellsViewModel.cellNumText.value,
                        onValueChange = {
                            cellsViewModel.cellNumText.value = it
                        }
                    )

                    MyVerticalSpacer(height = 5)

                    MyOutlineTextFiled(
                        label = "Number of Chicken",
                        keyboardType = KeyboardType.Number ,
                        initialText = cellsViewModel.henCountText.value,
                        onValueChange = {
                            cellsViewModel.henCountText.value = it
                        }
                    )

                }
            },
            confirmButton = {
                Button(onClick = {
                    cellsViewModel.updateCellInfo(
                        Cells(
                            cellId = cell.cellId,
                            blockId = cell.blockId,
                            cellNum = cellsViewModel.cellNumText.value.toInt(),
                            henCount = cellsViewModel.henCountText.value.toInt()
                        )
                    )
                    Toast.makeText(
                        context,
                        "Cell number ${cell.cellNum} Info updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
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

    var showAddCellDialog by remember { mutableStateOf(false) }
    MyInputDialog(
        showDialog = showAddCellDialog,
        title = "Create New Cell",
        onConfirm = { 
                    cellsViewModel.onAddNewCell(Cells(
                        blockId = block.blockId,
                        cellNum = if (listOfCells.isNotEmpty()) listOfCells.size + 1 else 1
                    ))
            showAddCellDialog = false
        },
        onDismiss = {
            showAddCellDialog = false
        }
    ) {
        Column {
            Text(text = "Are you sure you want to add a new cell this block?")
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Cells") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription ="Back" )
                    }
                }
            )},
        floatingActionButton = {
            if(userRole != "Collector") {
                IconButton(
                    onClick = {
                        showAddCellDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add"
                    )
                }
            }
        }
    ){ paddingValues->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                MyCard(modifier = Modifier.fillMaxWidth()
                    .padding(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "Block Number : ${block.blockNum}")
                        Text(text = "Number of cells : ${listOfCells.size}")
                    }
                }


//                MyVerticalSpacer(height = 3)

                LazyColumn(
                    modifier = Modifier.padding(3.dp)
                ) {
                    itemsIndexed(listOfCells) { _, item ->
                        MyVerticalSpacer(height = 6)

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(
                                        (0.03 * LocalConfiguration.current.screenWidthDp).dp
                                    )
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        cellsViewModel.setTheSelectedCell(item)
                                        cellsViewModel.showDialog.value = true
                                    }
                                    .fillMaxWidth(0.9f)

                                    .padding(6.dp)
                            ) {
                               // Text(text = "Cell Id : ${item.cellId}")
                                Text(text = "Cell number : ${item.cellNum}")
                                Text(text = "Number of Chicken : ${item.henCount}")
                            }

                            // MyHorizontalSpacer(width = 5)

                            var showDeleteDialog by remember { mutableStateOf(false) }
                            MyInputDialog(
                                showDialog = showDeleteDialog,
                                title = "Delete Block",
                                onConfirm = {
                                    // on delete block
                                    val cellNum = item.cellNum
                                    val cellId = item.cellId
                                    cellsViewModel.onDeleteCell(item)

                                    Toast.makeText(
                                        context,
                                        "Cell id: $cellId number: $cellNum deleted successfully",
                                        Toast
                                            .LENGTH_SHORT
                                    ).show()
                                    showDeleteDialog = false

                                },
                                onDismiss = {
                                    showDeleteDialog = false
                                }
                            )
                            {
                                Column {
                                    Text(text = "Warning! \nDeleting this block will also delete all the cells within the block ")
                                    MyVerticalSpacer(height = 10)
                                    Text(text = "Are you sure you want to delete?")
                                }
                            }

                            if(userRole != "Collector") {

                                IconButton(
                                    onClick = {
                                        //On delete cell
                                        showDeleteDialog = true
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "delete"
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}