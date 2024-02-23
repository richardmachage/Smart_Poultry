package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlocksDropDownMenu( //for Blocks
    listOfItems: List<BlocksWithCells>,
    onItemClick: (List<Cells>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedText by remember { mutableStateOf("-") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        TextField(
            modifier = modifier.menuAnchor(),
            value = "Block $selectedText",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOfItems.forEach { block ->
                DropdownMenuItem(
                    text = { Text(text = "Block ${block.block.blockNum}") },
                    onClick = {
                        selectedText = block.block.blockNum.toString()
                        onItemClick(block.cell)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CellsDropDownMenu( // for cells
    listOfItems: List<Cells>,
    onItemClick: (Cells) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedText by remember { mutableStateOf("-") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        TextField(
            modifier = modifier.menuAnchor(),
            value = "Cell $selectedText",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOfItems.forEach { cell ->
                DropdownMenuItem(
                    text = { Text(text = "Cell ${cell.cellNum}") },
                    onClick = {
                        selectedText = cell.cellNum.toString()
                        onItemClick(cell)
                        expanded = false
                    }
                )
            }
        }
    }

}