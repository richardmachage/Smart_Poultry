package com.example.smartpoultry.presentation.screens.viewRecordsScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.presentation.composables.MyBorderedColumn
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ViewRecordsScreen(
    navigator: DestinationsNavigator
) {
    val recordsViewModel = hiltViewModel<ViewRecordsViewModel>()
    val listOfRecords = recordsViewModel.getAllRecords().collectAsState(initial = emptyList())
    val context = LocalContext.current
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
                // scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {

            LazyColumn(modifier = Modifier.padding(6.dp)) {
                itemsIndexed(listOfRecords.value) { _, item ->
                    MyVerticalSpacer(height = 10)
                    val cell = recordsViewModel.getCell(item.cellId)
                    MyBorderedColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Date: ${item.date}")
                        Text(text = "BlockID: ${cell?.blockId}")
                        Text(text = "Cell num: ${cell?.cellNum}")
                        Text(text = "Eggs collected: ${item.eggCount}")
                        Text(text = "Chicken on this day: ${item.henCount}")
                    }
                }
            }
        }
    }
}

