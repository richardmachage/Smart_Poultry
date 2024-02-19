package com.example.smartpoultry.presentation.screens.cells

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    //val listOfBLocks by cellsViewModel.listOfBlocks.collectAsState()
    val listOfCells by cellsViewModel.getCellsForBLock(blockId).collectAsState()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ){
            Text(text = "The Block ID is : $blockId")
          //  Text(text = "Number of blocks is : ${listOfBLocks.size}")
            Text(text = "Number of cells is : ${listOfCells.size}")
        }

        /*LazyColumn{
            itemsIndexed(listOfCells){
                index, item ->
                Text(text = "Cell number : ${item.cellNum}")
                Text(text = "Number of Chicken : ${item.henCount}")
            }
        }*/
    }
}