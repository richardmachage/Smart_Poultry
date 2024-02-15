package com.example.smartpoultry.presentation.screens.eggCollection

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.screens.composables.MyBlocks
import com.example.smartpoultry.presentation.screens.composables.MyCardEggCollection
import com.example.smartpoultry.presentation.screens.composables.MyCells
import com.example.smartpoultry.presentation.screens.composables.MyDatePicker
import com.example.smartpoultry.presentation.screens.composables.MySimpleEditText
import com.example.smartpoultry.presentation.screens.composables.MySingleBlock
import com.example.smartpoultry.presentation.screens.composables.NormButton
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Destination
@Composable
fun EggScreen(
    //modifier: Modifier
){

    val eggViewModel = hiltViewModel<EggScreenViewModel>()
    val dateDialogState = rememberMaterialDialogState()


    Surface (
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){

        Column (
            modifier = Modifier
                .fillMaxSize()
        ){
            //Defining the datePicker first
            Row (
                modifier=Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Date: ",
                    fontSize = 20.sp
                )
                Text(
                    text = DateTimeFormatter
                        .ofPattern("dd MMM yyyy")
                        .format(eggViewModel.selectedDate.value),
                    fontSize = 20.sp
                )

                IconButton(
                    onClick = {
                        dateDialogState.show()
                    }
                ) {
                    Icon(imageVector = Icons.Default.DateRange,
                        contentDescription ="Calender" )
                }
            }
            MaterialDialog(
                dialogState =  dateDialogState,
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                ),
                buttons = {
                    positiveButton(text = "Ok")
                    negativeButton(text = "Cancel")
                }
            ) {
                datepicker(
                    colors = DatePickerDefaults.colors(
                        headerBackgroundColor = MaterialTheme.colorScheme.onPrimary,
                        headerTextColor = MaterialTheme.colorScheme.primary,
                        dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,
                        dateActiveTextColor = MaterialTheme.colorScheme.primary,

                        ),
                    initialDate = eggViewModel.selectedDate.value,
                    title = "Select Date"
                ){chosenDate->
                    eggViewModel.setSelectedDate(chosenDate)
                }

            }


            //Blocks and cells begin here
            LazyColumn{
                items(10){ blockNumber->

                    var totalEggCount by remember {
                        mutableStateOf("")
                    }

                    Column(
                        modifier = Modifier
                            .padding(4.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(
                                    (0.03 * LocalConfiguration.current.screenWidthDp).dp
                                )
                            )
                    ) {
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
                                    text = "Block : $blockNumber"
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(6.dp),
                                    text = "Total Eggs: $totalEggCount"
                                )

                            }

                           //This are the cell cards
                            //MyCells(numOfCells = numberOfCells)
                            LazyRow {
                                items(10) { cellNumber ->
                                    Card(
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .shadow(
                                                elevation = 10.dp,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .width(
                                                (LocalConfiguration.current.screenWidthDp / 4).dp
                                            )

                                    ) {
                                        Text(
                                            text = "Cell: $cellNumber",
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .align(Alignment.CenterHorizontally)
                                        )

                                        MySimpleEditText(
                                            keyboardType = KeyboardType.Number,
                                            iconLeading = ImageVector.vectorResource(R.drawable.egg_outline),
                                            iconLeadingDescription = "Eggs Icon",
                                            modifier = Modifier
                                                .padding(6.dp)
                                        )
                                    }

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
            }

            MyBlocks(numOfBlocks = 20)
        }
   }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevEgg(){
    SmartPoultryTheme {
       EggScreen()
    }
}