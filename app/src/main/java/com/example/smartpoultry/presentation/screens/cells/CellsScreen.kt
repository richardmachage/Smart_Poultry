package com.example.smartpoultry.presentation.screens.cells

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun CellsScreen(
    blockId : Int
){
    val cellsViewModel = hiltViewModel<CellsViewModel>()
    //val  listOfCells = cellsViewModel.blocksWithCells[0].cells
    println("Block Index : ")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(text = "BLockId : $blockId")
        /*LazyColumn{
            itemsIndexed(listOfCells){
                index, item ->
                Text(text = "Cell number : ${item.cellNum}")
                Text(text = "Number of Chicken : ${item.henCount}")
            }
        }*/
    }
}