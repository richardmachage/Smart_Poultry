package com.forsythe.smartpoultry.presentation.screens.cells

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.presentation.composables.buttons.MyFloatingActionButton
import com.forsythe.smartpoultry.presentation.composables.cards.MyCard
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.text.TitleText
import com.forsythe.smartpoultry.presentation.composables.textInputFields.MyOutlineTextFiled
import com.forsythe.smartpoultry.presentation.uiModels.BlockParse
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination
@Composable
fun CellsScreen(
    navigator: DestinationsNavigator,
    //blockId: Int
    block: BlockParse
) {
    val cellsViewModel = hiltViewModel<CellsViewModel>()

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
                        keyboardType = KeyboardType.Number,
                        initialText = cellsViewModel.cellNumText.value,
                        onValueChange = {
                            cellsViewModel.cellNumText.value = it
                        }
                    )

                    MyVerticalSpacer(height = 5)

                    MyOutlineTextFiled(
                        label = "Number of Chicken",
                        keyboardType = KeyboardType.Number,
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
            cellsViewModel.onAddNewCell(
                Cells(
                    blockId = block.blockId,
                    cellNum = if (listOfCells.isNotEmpty()) listOfCells.size + 1 else 1
                )
            )
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Cells") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (cellsViewModel.getManageBlockCellsAccess()/*userRole != "Collector"*/) {
                MyFloatingActionButton(
                    modifier = Modifier.padding(bottom = 50.dp),
                    onClick = {
                        showAddCellDialog = true
                    },
                    icon = {
                        Icon(
                            //modifier = Modifier.size(40.dp),
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add"
                        )
                    },
                    text = {
                        Text(text = "Add Cell")
                    }
                )

            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            //color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 6.dp, end = 16.dp),
                )
                {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 6.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleText(text = "Total Chicken : ${listOfCells.sumOf { it.henCount }}")
                        /*TitleText(
                            modifier = Modifier.weight(1f),
                            text = "Block ${block.blockNum}"
                        )
                        TitleText(
                            modifier = Modifier.weight(0.3f),
                            text = "||")
                        //TitleText(text = "Cells : ${listOfCells.size}")
                        TitleText(modifier = Modifier.weight(0.9f),
                            text = "Chicken ${listOfCells.sumOf { it.henCount }}"
                        )*/

                    }

                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                    }

                }



                LazyColumn(
                    modifier = Modifier.padding(4.dp),
                ) {
                    itemsIndexed(
                        listOfCells.sortedBy { it.cellNum },
                        key = { _, item -> item.cellId }) { _, item ->
                        //MyVerticalSpacer(height = 6)

                        MyCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    // .weight(1f),
                                ) {
                                    Text(
                                        modifier = Modifier.padding(6.dp),
                                        text = " " + item.cellNum + " ",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .clickable {
                                            if (cellsViewModel.getEditHenCountAccess()) {
                                                cellsViewModel.setTheSelectedCell(item)
                                                cellsViewModel.showDialog.value = true
                                            }
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

                                if (cellsViewModel.getManageBlockCellsAccess()/*userRole != "Collector"*/) {

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
}