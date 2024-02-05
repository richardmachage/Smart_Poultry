package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme


@Composable
fun MyCardEggCollection(
    cellNumber : Int,
){
    Card (
        modifier = Modifier
            .padding(6.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp)

            )
            .width(
                (LocalConfiguration.current.screenWidthDp / 4).dp
            )
            .height(90.dp)

    ){
        Text(
            text = "Cell: $cellNumber",
            modifier = Modifier
                .padding(6.dp)
                .align(Alignment.CenterHorizontally)
        )

        MySimpleEditText(
            keyboardType = KeyboardType.Number,
            iconLeading = ImageVector.vectorResource(R.drawable.egg_outline),
            iconLeadingDescription ="Eggs Icon",
            modifier = Modifier
                .padding(6.dp)
        )
    }
}

@Composable
fun MySingleBlock(
    blockNumber : Int,
    numberOfCells: Int
){
    var totalEggCount by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(
                    (0.03 * LocalConfiguration.current.screenWidthDp).dp
                ))
            //.background(color = MaterialTheme.colorScheme.background),
    ){
        Column(

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = "Block : $blockNumber")
                
                Text(modifier = Modifier
                    .padding(6.dp),
                    text = "Total Eggs: $totalEggCount")

            }

            Row( // This row holds the cell ards
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..numberOfCells){
                    MyCardEggCollection(cellNumber = i)
                }
            }
            NormButton(
                onButtonClick = { /*TODO*/ },
                btnName = "Save",
                modifier = Modifier
            )
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevEggCollection(){
    SmartPoultryTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column (
                modifier = Modifier
                    .padding(6.dp)
                    .verticalScroll(rememberScrollState())
            ){
                MySingleBlock(blockNumber = 1, numberOfCells =12 )
                MySingleBlock(blockNumber = 2, numberOfCells =12 )
                MySingleBlock(blockNumber = 3, numberOfCells =12 )
                MySingleBlock(blockNumber = 4, numberOfCells =12 )
            }
        }

    }

}