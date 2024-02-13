package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.presentation.screens.blockCellScreen.BlockCellViewModel
import com.example.smartpoultry.presentation.uiModels.BlockItem


@Composable
fun MyBlockInputDialog(viewModel: BlockCellViewModel = hiltViewModel()) {
    var showBlockDialog by remember { mutableStateOf(true) }
    var blockNumber by remember { mutableStateOf("") }
    var totalCells by remember { mutableStateOf("") }

    if (showBlockDialog) {
        Dialog(onDismissRequest = { showBlockDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextField(
                        value = blockNumber,
                        onValueChange = { blockNumber = it },
                        label = { Text("Block Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    TextField(
                        value = totalCells,
                        onValueChange = { totalCells = it },
                        label = { Text("Number of Cells") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { showBlockDialog = false }) {
                            Text("Cancel")
                        }
                        //Spacer(Modifier.width(8.dp))
                        MyHorizontalSpacer(width = 8)

                        Button(onClick = {
                            if ( blockNumber.isNotBlank() or totalCells.isNotBlank()) {
                                val blockItem = BlockItem(
                                    blockNum = blockNumber.toInt(),
                                    numberOfCells = totalCells.toInt()
                                )
                            } else {

                            }
                            //viewModel.addNewBlock(block)
                            showBlockDialog = false
                        }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MyDialogPrev(){
    MyBlockInputDialog()
}