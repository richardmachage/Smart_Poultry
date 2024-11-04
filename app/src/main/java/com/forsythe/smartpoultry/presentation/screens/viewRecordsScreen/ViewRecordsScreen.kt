package com.forsythe.smartpoultry.presentation.screens.viewRecordsScreen

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.forsythe.smartpoultry.data.dataModels.EggRecordFull
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.others.MyBorderedRow
import com.forsythe.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination
@Composable
fun ViewRecordsScreen(
    navigator: DestinationsNavigator
) {
    val recordsViewModel = hiltViewModel<ViewRecordsViewModel>()
    /*val listOfRecordsFull =
        recordsViewModel.getAllFullRecords().collectAsState(initial = emptyList())
*/
    val listOfAllRecords : LazyPagingItems<EggRecordFull> = recordsViewModel.getAllFullRecords().collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(key1 = recordsViewModel.toastMessage.value) {
        if (recordsViewModel.toastMessage.value.isNotBlank()){
            Toast.makeText(context, recordsViewModel.toastMessage.value, Toast.LENGTH_SHORT).show()
            recordsViewModel.toastMessage.value = ""
        }
    }
    MyCircularProgressBar(isLoading = recordsViewModel.isLoading.value, displayText = "Exporting Excel..")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Egg Collection Records") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                      //  recordsViewModel.onExportToExcel(context = context, listOfRecords = listOfRecordsFull.value)
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "export")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column {
                var queryValue by remember { mutableStateOf("") }
                var active by remember { mutableStateOf(false) }
                //var items = remember { mutableStateListOf(listOfRecords) }
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    query = queryValue,
                    onQueryChange = { newQuery ->
                        queryValue = newQuery
                    },
                    onSearch = {
                        active = false
                    },
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
                    },
                    windowInsets = WindowInsets.systemBars.exclude(WindowInsets.statusBars)
                ) {
                    //SearchBar Content
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {

                        LazyColumn(modifier = Modifier
                            .padding(6.dp)
                            .animateContentSize()) {
                            itemsIndexed(
                                recordsViewModel.searchRecord(
                                    queryValue,
                                    //listOfRecordsFull.value
                                    listOfAllRecords.itemSnapshotList.toList().filterNotNull()
                                ),
                                key = {_, item -> item.productionId }
                            ) { _, item ->
                                var showDeleteDialog by remember{ mutableStateOf(false)}
                                MyInputDialog(
                                    showDialog= showDeleteDialog,
                                    title = "Delete Record",
                                    onConfirm = {
                                        recordsViewModel.onDeleteRecord(item.productionId)
                                        showDeleteDialog = false
                                    },
                                    onDismiss = {showDeleteDialog = false}
                                ) {
                                    Text(text = "Delete record for block ${item.blockNum} cell ${item.cellNum} date ${item.date} ?")
                                }
                                MyVerticalSpacer(height = 5)
                                //val cell = recordsViewModel.getCell(item.cellId)
                                MyBorderedRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItemPlacement(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column (
                                    ) {
                                        Text(text = "Date: ${item.date}")
                                        Text(text = "Block: ${item.blockNum}")
                                        Text(text = "Cell : ${item.cellNum}")
                                        Text(text = "Eggs collected on this day: ${item.eggCount}")
                                        Text(text = "Chicken on this day: ${item.henCount}")
                                    }

                                    IconButton(onClick = {
                                        showDeleteDialog = true
                                    }) {
                                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
                                    }
                                }
                            }
                        }
                    }
                }
                //General List
                LazyColumn(modifier = Modifier.padding(6.dp)) {

                    items(
                        count = listOfAllRecords.itemCount
                    ){index ->
                        val record = listOfAllRecords[index]
                        record?.let { item->
                            var showDeleteDialog by remember{ mutableStateOf(false)}
                            MyInputDialog(
                                showDialog= showDeleteDialog,
                                title = "Delete Record",
                                onConfirm = {
                                    recordsViewModel.onDeleteRecord(item.productionId)
                                    showDeleteDialog = false
                                },
                                onDismiss = {showDeleteDialog = false}
                            ) {
                                Text(text = "Delete record for block ${item.blockNum} cell ${item.cellNum} date ${item.date} ?")
                            }

                            MyVerticalSpacer(height = 10)

                            MyBorderedRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column (
                                ) {
                                    Text(text = "Date: ${item.date}")
                                    Text(text = "Block: ${item.blockNum}")
                                    Text(text = "Cell : ${item.cellNum}")
                                    Text(text = "Eggs collected on this day: ${item.eggCount}")
                                    Text(text = "Chicken on this day: ${item.henCount}")
                                }

                                IconButton(onClick = {showDeleteDialog = true}) {
                                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
                                }
                            }
                        }

                    }
                    /*itemsIndexed(listOfRecordsFull.value, key = {_, item ->  item.productionId}) { _, item ->


                    }*/
                }
            }

        }
    }
}


