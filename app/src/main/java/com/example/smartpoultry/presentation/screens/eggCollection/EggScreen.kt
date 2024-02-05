package com.example.smartpoultry.presentation.screens.eggCollection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartpoultry.presentation.screens.composables.MyDatePicker
import com.example.smartpoultry.presentation.screens.composables.MySingleBlock
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EggScreen(
    modifier: Modifier
){

    var eggViewModel = viewModel<EggScreenViewModel>()
    var dateDialogState = rememberMaterialDialogState()


    Surface (
        modifier = modifier
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                for(block in 1..10){
                    MySingleBlock(blockNumber = block, numberOfCells = 12)
                }
            }
        }
   }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevEgg(){
    SmartPoultryTheme {
        EggScreen(modifier = Modifier)
    }
}