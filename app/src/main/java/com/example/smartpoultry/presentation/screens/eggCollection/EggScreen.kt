package com.example.smartpoultry.presentation.screens.eggCollection

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.CircularProgressBar
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.example.smartpoultry.presentation.uiModels.CellEggCollection
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun EggScreen() {

    val eggViewModel = hiltViewModel<EggScreenViewModel>()
    val dateDialogState = rememberMaterialDialogState()
    val listOfBlocks = eggViewModel.myInputBlocks
    val context = LocalContext.current
    //val listOfBlocksDB = eggViewModel.getAllBlocks.collectAsState()

    CircularProgressBar(isLoading = eggViewModel.isLoading.value)

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //Defining the datePicker first
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Calender"
                    )
                }
            }
            MaterialDialog(
                dialogState = dateDialogState,
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
                    title = "Select Date",
                    allowedDateValidator = {
                        it < LocalDate.now() || it == LocalDate.now()
                    }
                ) { chosenDate ->
                    eggViewModel.setChosenDateValue(chosenDate)
                    eggViewModel.setSelectedDate(chosenDate)
                }

            }


            //Blocks and cells UI begins here
            LazyColumn { //Column of Blocks
                itemsIndexed(listOfBlocks) { blockIndex, block ->

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
                                    text = "Block : ${listOfBlocks[blockIndex].blockId}"
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(6.dp),
                                    text = "Total Eggs: ${listOfBlocks[blockIndex].cells.sumOf { cellEggCollection: CellEggCollection -> cellEggCollection.eggCount }}"
                                )

                            }

                            //This are the cell cards
                            //MyCells(numOfCells = numberOfCells)
                            LazyRow {
                                itemsIndexed(listOfBlocks[blockIndex].cells) { cellIndex, cell ->
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
                                            text = "Cell: ${listOfBlocks[blockIndex].cells[cellIndex].cellNum}",
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .align(Alignment.CenterHorizontally)
                                        )

                                        var textFieldValueState by remember {
                                            mutableStateOf(TextFieldValue(text = listOfBlocks[blockIndex].cells[cellIndex].eggCount.toString()))
                                        }
                                        OutlinedTextField(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            value = textFieldValueState,//TextFieldValue(listOfBlocks[blockIndex].cells[cellIndex].eggCount.toString()),//)cell.eggCount.toString()),
                                            onValueChange = { newText ->
                                                textFieldValueState = newText.copy(
                                                    text = newText.text,
                                                    selection = TextRange(newText.text.length)
                                                )
                                                val newEggCount = newText.text.toIntOrNull() ?: 0
                                                eggViewModel.updateEggCount(
                                                    blockIndex,
                                                    cellIndex,
                                                    newEggCount
                                                )
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = ImageVector.vectorResource(R.drawable.egg_outline),
                                                    contentDescription = "egg"
                                                )
                                            },
                                            singleLine = true,
                                        )
                                    }

                                }
                            }

                            NormButton(
                                onButtonClick = {
                                    eggViewModel.isLoading.value = true
                                   // eggViewModel.delayApp(2000)

                                    eggViewModel.onSaveRecord(block = blockIndex, cellsInput = listOfBlocks[blockIndex].cells)
                                    //eggViewModel.updateEggCount(blockIndex,)
                                    if (eggViewModel.insertStatus.value){
                                        Toast.makeText(
                                            context,
                                            "records for Block: ${listOfBlocks[blockIndex].blockNum} saved successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }else{
                                        Toast.makeText(
                                            context,
                                            "Failed! Records for Block: ${listOfBlocks[blockIndex].blockNum} for date: ${eggViewModel.selectedDate.value} already exist",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    eggViewModel.isLoading.value = false

                                },
                                btnName = "Save",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                }
            }

            //MyBlocks(numOfBlocks = 20)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevEgg() {
    SmartPoultryTheme {
       // EggScreen()
    }
}