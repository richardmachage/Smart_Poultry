package com.forsythe.smartpoultry.presentation.screens.eggCollection

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.presentation.composables.buttons.MyOutlineButton
import com.forsythe.smartpoultry.presentation.composables.cards.MyCard
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.others.MyBorderedColumn
import com.forsythe.smartpoultry.presentation.composables.others.MyDatePicker
import com.forsythe.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.forsythe.smartpoultry.presentation.uiModels.CellEggCollection
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun EggScreen(
    navigator: DestinationsNavigator
) {

    val eggViewModel = hiltViewModel<EggScreenViewModel>()
    val listOfBlocks = remember { eggViewModel.myInputBlocks }
    val context = LocalContext.current

    LaunchedEffect(eggViewModel.toastMessage.value) {
        if (eggViewModel.toastMessage.value.isNotBlank()) {
            Toast.makeText(context, eggViewModel.toastMessage.value, Toast.LENGTH_SHORT).show()
            eggViewModel.toastMessage.value = ""
        }
    }

    MyCircularProgressBar(
        isLoading = eggViewModel.isLoading.value,
        displayText = "saving"
    )

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        var switchModes by remember { mutableStateOf(true) }
        Column {
            MyOutlineButton(
                modifier = Modifier.fillMaxWidth(),
                onButtonClick = { switchModes = !switchModes },
                btnName = if (!switchModes) "Switch to input Per Cell" else "Switch to input Per Block"
            )
            AnimatedContent(targetState = switchModes, label = "eggCollectionMode") {state->
                if (state) {
                    Column(
                        modifier = Modifier
                    ) {
                        MyDatePicker(
                            modifier = Modifier
                                .fillMaxWidth(),
                            dateDialogState = rememberMaterialDialogState(),
                            label = "Date",
                            positiveButtonOnClick = { chosenDate ->
                                eggViewModel.setChosenDateValue(chosenDate)
                                eggViewModel.setSelectedDate(chosenDate)
                            },
                            negativeButton = {},
                            allowedDateValidate = { date->
                                date < LocalDate.now() || date == LocalDate.now()
                            }
                        )
                        var showZeroEggsDialog by remember { mutableStateOf(false) }
                        MyInputDialog(
                            showDialog = showZeroEggsDialog,
                            title = "No eggs Collected",
                            onConfirm = {
                                eggViewModel.zeroCellEggs?.let {thisCell->
                                    eggViewModel.onSaveSingleCellRecord(
                                        cell = thisCell,
                                        eggCount = 0
                                    )
                                    showZeroEggsDialog = false
                                }
                            },
                            onDismiss = {
                                showZeroEggsDialog = false
                            }
                        ) {
                            Column {
                                Text(text = "There is no eggs collected from this cell?")
                                Text(text = "This will record 0 egg count.")
                            }
                        }
                        EggCollectionScreen(
                            listOfBlocks = eggViewModel.getAllBlocks.collectAsState().value,
                            onSave = { cell, eggCount ->
                                if (eggCount == 0) {
                                    eggViewModel.zeroCellEggs = cell
                                    showZeroEggsDialog = true
                                } else{
                                    eggViewModel.onSaveSingleCellRecord(cell,eggCount)
                                }
                            }
                        )
                    }
                }
                else {
                    Column(
                        modifier = Modifier
                        // .fillMaxSize()
                    ) {
                        //Defining the datePicker
                        MyDatePicker(
                            modifier = Modifier
                                .fillMaxWidth(),
                            dateDialogState = rememberMaterialDialogState(),
                            label = "Date",
                            positiveButtonOnClick = { chosenDate ->
                                eggViewModel.setChosenDateValue(chosenDate)
                                eggViewModel.setSelectedDate(chosenDate)
                            },
                            negativeButton = {},
                            allowedDateValidate = {
                                it < LocalDate.now() || it == LocalDate.now()
                            }
                        )

                        //Blocks and cells UI begins here
                        LazyColumn { //Column of Blocks
                            itemsIndexed(listOfBlocks) { blockIndex, block ->
                                MyBorderedColumn(
                                    modifier = Modifier
                                        .padding(4.dp)

                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {

                                            Text(
                                                modifier = Modifier
                                                    .padding(6.dp),
                                                text = "Block : ${listOfBlocks[blockIndex].blockNum}"
                                            )

                                            Text(
                                                modifier = Modifier
                                                    .padding(6.dp),
                                                text = "Total Eggs: ${listOfBlocks[blockIndex].cells.sumOf { cellEggCollection: CellEggCollection -> cellEggCollection.eggCount }}"
                                            )
                                        }

                                        //These are the cell cards
                                        var saveButtonState by remember { mutableStateOf(true) }
                                        LazyRow {
                                            itemsIndexed(listOfBlocks[blockIndex].cells) { cellIndex, cell ->

                                                MyCard(
                                                    modifier = Modifier.padding(4.dp)
                                                ) {
                                                    Text(
                                                        text = "Cell :${cell.cellNum}",
                                                        modifier = Modifier
                                                            //.padding(6.dp)
                                                            .align(Alignment.CenterHorizontally)
                                                    )
                                                    Text(
                                                        text = "Chicken: ${cell.henCount}",//${listOfBlocks[blockIndex].cells[cellIndex].cellNum}",
                                                        modifier = Modifier
                                                            .padding(3.dp)
                                                            .align(Alignment.CenterHorizontally)
                                                    )

                                                    var textFieldValueState by remember {
                                                        mutableStateOf(
                                                            TextFieldValue(text = listOfBlocks[blockIndex].cells[cellIndex].eggCount.toString())
                                                        )
                                                    }
                                                    var isErrorState by remember {
                                                        mutableStateOf(
                                                            false
                                                        )
                                                    }
                                                    OutlinedTextField(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(5.dp),
                                                        value = textFieldValueState,//TextFieldValue(listOfBlocks[blockIndex].cells[cellIndex].eggCount.toString()),//)cell.eggCount.toString()),
                                                        onValueChange = { newText ->
                                                            textFieldValueState = newText.copy(
                                                                text = newText.text,
                                                                selection = TextRange(newText.text.length)
                                                            )

                                                            val newEggCount =
                                                                newText.text.toIntOrNull() ?: 0

                                                            if (newEggCount > cell.henCount) {
                                                                isErrorState = true
                                                                saveButtonState = false
                                                            } else {
                                                                isErrorState = false
                                                                saveButtonState = true
                                                                eggViewModel.updateEggCount(
                                                                    blockIndex,
                                                                    cellIndex,
                                                                    newEggCount
                                                                )
                                                            }
                                                        },
                                                        keyboardOptions = KeyboardOptions(
                                                            keyboardType = KeyboardType.Number
                                                        ),
                                                        leadingIcon = {
                                                            Icon(
                                                                imageVector = ImageVector.vectorResource(
                                                                    R.drawable.egg_outline
                                                                ),
                                                                contentDescription = "egg"
                                                            )
                                                        },
                                                        singleLine = true,
                                                        isError = isErrorState
                                                    )
                                                }

                                            }
                                        }

                                        MyOutlineButton(
                                            onButtonClick = {
                                                eggViewModel.onSaveRecord(
                                                    block = blockIndex,
                                                    cellsInput = listOfBlocks[blockIndex].cells
                                                )

                                            },
                                            btnName = "Save",
                                            modifier = Modifier.fillMaxWidth(),
                                            enabled = saveButtonState
                                        )
                                    }
                                }

                            }

                        }

                    }
                }
            }
        }
    }
}