package com.example.smartpoultry.presentation.screens.blockCellScreen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.destinations.CellsScreenDestination
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.uiModels.BlockItem
import com.example.smartpoultry.presentation.uiModels.BlockParse
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun BlockCellScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val blockCellViewModel: BlockCellViewModel = hiltViewModel()
    val userRole by blockCellViewModel.userRole.collectAsState()
    //val listOfBlocks by blockCellViewModel.listOfBlocks.collectAsState()
    val listOfBlocksWithCells by blockCellViewModel.listOfBlocksWithCells.collectAsState()

    val showDialog = blockCellViewModel.showDialog.value

    //AddBlockDialog
    if (showDialog) {
        // This dialog asks for the block number and number of cells.
        // Upon confirmation, it creates a BlockItem and calls onAddNewBlock.
        AlertDialog(
            onDismissRequest = {
                blockCellViewModel.showDialog.value = false
            },
            title = { Text(text = "Add New Block") },
            text = {
                Column {
                    MyOutlineTextFiled(
                        label = "Block Number",
                        keyboardType = KeyboardType.Number ,
                        initialText = blockCellViewModel.blockNumText.value,
                        onValueChange = { blockCellViewModel.blockNumText.value = it },
                    )
                    /*TextField(
                        value = blockCellViewModel.blockNumText.value,
                        onValueChange = { blockCellViewModel.blockNumText.value = it },
                        label = { Text(text = "Block Number") },
                    )*/
                    MyVerticalSpacer(height = 5)

                    MyOutlineTextFiled(
                        label = "Number of Cells",
                        keyboardType = KeyboardType.Number ,
                        initialText = blockCellViewModel.cellsText.value,
                        onValueChange = { blockCellViewModel.cellsText.value = it },
                    )
                    /*TextField(
                        value = blockCellViewModel.cellsText.value,
                        onValueChange = { blockCellViewModel.cellsText.value = it },
                        label = { Text(text = "Number of cells") },
                    )*/
                }
            },

            confirmButton = {
                Button(onClick = {
                    if (blockCellViewModel.blockNumText.value.isNotBlank() && blockCellViewModel.cellsText.value.isNotBlank()) {
                        blockCellViewModel.onAddNewBlock(
                            BlockItem(
                                blockNum = blockCellViewModel.blockNumText.value.toInt(),
                                numberOfCells = blockCellViewModel.cellsText.value.toInt()
                            )
                        )
                        blockCellViewModel.showDialog.value = false
                        blockCellViewModel.clearTextFields()
                    } else {
                        Toast.makeText(context, "Empty Fields", Toast.LENGTH_SHORT).show()
                    }

                }) {
                    Text(text = "Add")
                }
            },
            dismissButton = {
                Button(onClick = { blockCellViewModel.showDialog.value = false }) {
                    Text("Cancel")
                }
            }

        )
    }

    Scaffold(
        //if (userRole != "Collector")
        floatingActionButton = {
            if(userRole != "Collector") {
                IconButton(
                    onClick = {
                        //I want the dialog to show when this button is clicked
                        blockCellViewModel.showDialog.value = true
                        //   Toast.makeText(context,"new block added", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add"
                    )
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(6.dp),
                contentPadding = paddingValues
            ) {
                itemsIndexed( listOfBlocksWithCells//listOfBlocks
                ) { blockIndex, blockWithCells ->
                    MyVerticalSpacer(height = 10)
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
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier
                                .clickable {
                                    navigator.navigate(
                                        CellsScreenDestination(
                                            BlockParse(
                                                blockId = blockWithCells.block.blockId,
                                                blockNum = blockWithCells.block.blockNum,
                                                totalCells = blockWithCells.cell.size
                                            )
                                        )
                                    )
                                }
                                .fillMaxWidth(0.9f)
                                .padding(6.dp)
                        ) {
                          //  Text(text = "BlockId : ${blockWithCells.block.blockId}")
                            Text(text = "Block Number : ${blockWithCells.block.blockNum}")
                            Text(text = "Number of cells: ${blockWithCells.cell.size}")
                        }

                        // MyHorizontalSpacer(width = 5)


                        var showDeleteDialog by remember { mutableStateOf(false) }
                        MyInputDialog(
                            showDialog = showDeleteDialog,
                            title = "Delete Block",
                            onConfirm = {
                                // on delete block
                                val blockId = blockWithCells.block.blockId
                                val blockNum = blockWithCells.block.blockNum

                                CoroutineScope(Dispatchers.IO).launch {
                                    blockCellViewModel.onDeleteBlock(block = blockWithCells.block)
                                }

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

                            IconButton(onClick = {
                                //show confirm delete dialog
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


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevBlockCell() {
    //BlockCellScreen()
}