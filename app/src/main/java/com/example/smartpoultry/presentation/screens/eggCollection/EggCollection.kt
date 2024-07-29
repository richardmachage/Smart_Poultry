package com.example.smartpoultry.presentation.screens.eggCollection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.local.room.relations.BlocksWithCells
import com.example.smartpoultry.presentation.composables.BlocksDropDownMenu
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.screens.eggCollection.components.CellEggCollectionItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EggCollectionScreen(
    listOfBlocks: List<BlocksWithCells>,
    onSave : (cell : Cells, eggCount : Int) -> Unit  = {_,_ ->}
) {
    val listOfCells = remember { mutableStateListOf<Cells>() }
    var showCellsAnime by remember{ mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BlocksDropDownMenu(
            modifier = Modifier.fillMaxWidth(),
            listOfItems = listOfBlocks,
            onItemClick = { blockId, blockNum, cells ->
                showCellsAnime = false
                listOfCells.clear()
                listOfCells.addAll(cells)
                showCellsAnime = true
                /*onBlockSelected(BlocksWithCells(
                    block = Blocks(
                        blockId = blockId,
                        blockNum =  blockNum,
                        totalCells = cells.size
                    ),
                    cell = cells
                )
                )*/
            }
        )

        MyVerticalSpacer(height = 5)

        AnimatedVisibility(visible = showCellsAnime) {
                LazyColumn() {
                    items(listOfCells.toList().sortedBy { it.cellNum }, key = { it.cellId }) {cell->
                        CellEggCollectionItem(
                            modifier = Modifier.animateItemPlacement().padding(6.dp),
                            cellNum = cell.cellNum,
                            henCount = cell.henCount,
                            onSave = {eggCount->
                                onSave(cell, eggCount)}
                        )

                }
            }
        }
    }
}


val listOfBlocks = listOf("block 1", "block 2", "block 3")