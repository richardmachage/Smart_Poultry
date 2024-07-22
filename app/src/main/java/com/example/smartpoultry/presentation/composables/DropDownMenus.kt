package com.example.smartpoultry.presentation.composables

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.local.room.relations.BlocksWithCells

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlocksDropDownMenu( //for Blocks
    listOfItems: List<BlocksWithCells>,
    onItemClick: ( blockId: Int, blockNum: Int, cells : List<Cells>) -> Unit,
    modifier: Modifier = Modifier,
    width : Float = 0.4f
) {
    var selectedText by remember { mutableStateOf("-") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier
            .padding(3.dp)
            .fillMaxWidth(width),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        OutlinedTextField(
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
            ,
            value = "Block $selectedText",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            maxLines = 1,
            label = { Text(text = "Block")}
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
                        onItemClick(block.block.blockId, block.block.blockNum, block.cell)
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
        modifier = modifier
            .padding(3.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = "Cell $selectedText",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            label = { Text(text = "Cell")}
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    items : List<String> = emptyList(),
    defaultSelectedIndex : Int = 0,
    onItemClick: (String) -> Unit,
    modifier: Modifier,
    menuLabel : String = "",
){
    var selectedItem by remember { mutableStateOf(items[defaultSelectedIndex]) }
    var expanded by  remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
        OutlinedTextField(
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(start = (6.dp), end = (6.dp)),
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            label = { Text(text = menuLabel)},
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedItem = item
                        onItemClick(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTypeDropDownMenu(
    listOfItems: List<String> = listOf("Collector","Manager", "Director"),
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
){

    var selectedText by remember { mutableStateOf("-") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier
            .padding(3.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        OutlinedTextField(
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(start = (6.dp), end = (6.dp)),
            value = " $selectedText",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            label = { Text(text = "User Type")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User"
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOfItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedText = item
                        onItemClick(item)
                        expanded = false
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthsDropDownMenu(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
){
    val listOfMonths = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    )
    var selectedText by remember { mutableStateOf("-") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier
            .padding(3.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        OutlinedTextField(
            modifier = modifier
                .menuAnchor(),
              // .fillMaxWidth(0.5f)
            value = " $selectedText",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            label = { Text(text = "Month")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "User"
                )
            }
        )

        ExposedDropdownMenu(
            modifier = modifier.fillMaxHeight(0.3f),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOfMonths.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedText = item
                        onItemClick(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearsDropDownMenu(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
){
    val listOfYears = listOf("2024","2023")
    var selectedText by remember { mutableStateOf("-") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier
            .padding(3.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        OutlinedTextField(
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(0.5f),
            value = " $selectedText",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            label = { Text(text = "Year")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Year"
                )
            }
        )

        ExposedDropdownMenu(
           // modifier = modifier.fillMaxHeight(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOfYears.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedText = item
                        onItemClick(item)
                        expanded = false
                    }
                )
            }
        }
    }
}