package com.example.smartpoultry.presentation.screens.eggCollection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartpoultry.presentation.composables.DropDownMenu
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.screens.eggCollection.components.CellEggCollectionItem

@Composable
fun EggCollectionScreen() {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            DropDownMenu(
                modifier = Modifier.fillMaxWidth(),
                items = listOfBlocks,
                defaultValue = "Select Block",
                onItemClick = {},
            )

            MyVerticalSpacer(height = 5)

            LazyColumn(

            ) {
                items(10) {
                    CellEggCollectionItem(
                        cellNum = it + 1,
                        henCount = 3
                    )
                }
            }
        }
    }


val listOfBlocks = listOf("block 1", "block 2", "block 3")