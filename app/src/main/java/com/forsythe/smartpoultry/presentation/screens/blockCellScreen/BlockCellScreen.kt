package com.forsythe.smartpoultry.presentation.screens.blockCellScreen

import android.annotation.SuppressLint
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forsythe.smartpoultry.presentation.composables.ads.BannerAd
import com.forsythe.smartpoultry.presentation.composables.buttons.MyFloatingActionButton
import com.forsythe.smartpoultry.presentation.composables.cards.MyCard
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.textInputFields.MyOutlineTextFiled
import com.forsythe.smartpoultry.presentation.destinations.CellsScreenDestination
import com.forsythe.smartpoultry.presentation.uiModels.BlockItem
import com.forsythe.smartpoultry.presentation.uiModels.BlockParse
import com.forsythe.smartpoultry.utils.BANNER_AD_ID
import com.forsythe.smartpoultry.utils.isNetworkAvailable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun BlockCellScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val blockCellViewModel: BlockCellViewModel = hiltViewModel()

    val listOfBlocksWithCells by blockCellViewModel.listOfBlocksWithCells.collectAsState()

    val showDialog = blockCellViewModel.showDialog.value

    //AddBlockDialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                blockCellViewModel.showDialog.value = false
            },
            title = { Text(text = "Add New Block") },
            text = {
                Column {
                    MyOutlineTextFiled(
                        label = "Block Number",
                        keyboardType = KeyboardType.Number,
                        initialText = blockCellViewModel.blockNumText.value,
                        onValueChange = { blockCellViewModel.blockNumText.value = it },
                    )

                    MyVerticalSpacer(height = 5)

                    MyOutlineTextFiled(
                        label = "Number of Cells",
                        keyboardType = KeyboardType.Number,
                        initialText = blockCellViewModel.cellsText.value,
                        onValueChange = { blockCellViewModel.cellsText.value = it },
                    )
                }
            },

            confirmButton = {
                Button(onClick = {
                    if (blockCellViewModel.blockNumText.value.isNotBlank() && blockCellViewModel.cellsText.value.isNotBlank()) {
                        blockCellViewModel.onAddNewBlock(
                            BlockItem(
                                blockNum = blockCellViewModel.blockNumText.value.toInt(),
                                numberOfCells = blockCellViewModel.cellsText.value.toInt()
                            ),
                            isNetAvailable = context.isNetworkAvailable()
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
        floatingActionButton = {

            if (blockCellViewModel.getManageBlockCellsAccess()) {

                MyFloatingActionButton(
                    onClick = {
                        blockCellViewModel.showDialog.value = true
                    },
                    icon = {
                        Icon(
                            //modifier = Modifier.size(40.dp),
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add"
                        )
                    },
                    text = {
                        Text(text = "Add Block")
                    }
                )
            }
        },

    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //banner add
                if (
                    //TODO check if premium subscription is active
                    true
                ){
                    //show banner add
                    BannerAd(
                        adId = BANNER_AD_ID
                    )
                }
                LazyColumn(
                    modifier = Modifier,
                ) {
                    itemsIndexed(
                        listOfBlocksWithCells.sortedBy { it.block.blockNum },
                        key = { _, item -> item.block.blockId }
                    ) { _, blockWithCells ->

                        MyCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(3.dp)
                                    .animateItemPlacement(),
                                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
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
                                        text = " " + blockWithCells.block.blockNum.toString() + " ",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                }

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
                                        //.fillMaxWidth(0.9f)
                                        .padding(4.dp)
                                        .weight(1f)
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
                                if (blockCellViewModel.getManageBlockCellsAccess()/*userRole != "Collector"*/) {

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
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevBlockCell() {
    //BlockCellScreen()
}