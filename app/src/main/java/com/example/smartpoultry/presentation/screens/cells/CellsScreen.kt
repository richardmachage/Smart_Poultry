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
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun CellsScreen(){
    val cellsViewModel = hiltViewModel<CellsViewModel>()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn{
            itemsIndexed(cellsViewModel.blocksWithCells[0].cell){
                index, item ->
                Text(text = "Cell number : ${item.cellNum}")
                Text(text = "Number of Chiken : ${item.henCount}")
            }
        }
    }
}