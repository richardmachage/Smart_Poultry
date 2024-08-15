package com.forsythe.smartpoultry.presentation.screens.eggCollection

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.presentation.composables.buttons.MyOutlineButton
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.others.MyDatePicker
import com.forsythe.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.forsythe.smartpoultry.presentation.composables.text.TitleText
import com.forsythe.smartpoultry.presentation.uiModels.CellEggCollection
import com.forsythe.smartpoultry.utils.isNetworkAvailable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun EggScreen(
    navigator : DestinationsNavigator
) {

    val eggViewModel = hiltViewModel<EggScreenViewModel>()
    val listOfBlocks = remember { eggViewModel.myInputBlocks }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
//            val isNetAvailable = context.isNetworkAvailable().toString()
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
                                    scope.launch{eggViewModel.onSaveSingleCellRecord(
                                            cell = thisCell,
                                            eggCount = 0,
                                            isNetAvailable = context.isNetworkAvailable()
                                        )
                                        showZeroEggsDialog = false
                                    }
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
                        EggCollectionPerCell(
                            listOfBlocks = eggViewModel.getAllBlocks.collectAsState().value,
                            onSave = { cell, eggCount ->
                                if (eggCount == 0) {
                                    eggViewModel.zeroCellEggs = cell
                                    showZeroEggsDialog = true
                                } else{
                                     scope.launch {
                                        eggViewModel.onSaveSingleCellRecord(cell = cell, eggCount = eggCount, isNetAvailable = context.isNetworkAvailable())
                                    }
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
                        LazyColumn(
                            modifier = Modifier.padding(6.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) { //Column of Blocks
                            itemsIndexed(listOfBlocks) { blockIndex, block ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        //Inventory block
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surface)
                                           // .padding(8.dp),
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {

                                                TitleText(
                                                    modifier = Modifier
                                                        .padding(start = 16.dp),
                                                    fontSize = 20.sp,
                                                    text = "Block  ${listOfBlocks[blockIndex].blockNum}"
                                                )

                                                TitleText(
                                                    modifier = Modifier
                                                        .padding(end = 16.dp),
                                                    fontSize = 20.sp,
                                                    text = "${listOfBlocks[blockIndex].cells.sumOf { cellEggCollection: CellEggCollection -> cellEggCollection.eggCount }} Eggs"
                                                )
                                            }

                                            //These are the cell cards
                                            var saveButtonState by remember { mutableStateOf(true) }
                                            LazyRow(
                                                modifier = Modifier.padding(6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                itemsIndexed(listOfBlocks[blockIndex].cells) { cellIndex, cell ->

                                                    Card(
                                                        shape = RoundedCornerShape(16.dp),
                                                        elevation = CardDefaults.cardElevation(8.dp),
                                                        modifier = Modifier.width((LocalConfiguration.current.screenWidthDp / 4).dp),
                                                        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)

                                                    ) {
                                                        Text(
                                                            text = "Cell ${cell.cellNum}",
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
                                                        cellsInput = listOfBlocks[blockIndex].cells,
                                                        isNetAvailable = context.isNetworkAvailable()
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
}