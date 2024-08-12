package com.forsythe.smartpoultry.presentation.composables.searchBars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.forsythe.smartpoultry.data.dataModels.EggRecordFull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
   // isActive : Boolean = false
    listOfRecords : List<String> = listOf("moday","elena", "enola", "machine", "debryune"),
    searchRecord : () -> List<EggRecordFull>
) {
    var queryValue by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = remember { mutableStateListOf(listOfRecords) }
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = queryValue,
        onQueryChange = { newQuery ->
            queryValue = newQuery
        },
        onSearch = {
                   active = false
        } ,
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search")
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (queryValue.isNotBlank()) queryValue = ""
                    else active = false
                }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
            }
        }
    ) {

    }
}
