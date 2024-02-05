package com.example.smartpoultry.presentation.screens.composables

import android.widget.ImageButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun DatePicker() {
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .format(pickedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()

}

@Composable
fun MyDatePicker(
    dateDialogState : MaterialDialogState,
    positiveButton : () -> Unit,
    negativeButton : () -> Unit
){
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    Row (
        modifier = Modifier
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ){
        Text(
            text = DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .format(pickedDate), // date to be displayed by default
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

    MaterialDialog ( // defines a material dialog
        dialogState = dateDialogState,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
        buttons = {
            positiveButton(text = "OK"){
                //TODO what happens after ok is clicked?
                positiveButton()
            }

            negativeButton(text = "Cancel"){
                negativeButton()
            }

        }
    ){ // now here we specify the kind of material dialog such as date picker
        datepicker(
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.onPrimary,
                headerTextColor = MaterialTheme.colorScheme.primary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,
                dateActiveTextColor = MaterialTheme.colorScheme.primary,

            ),
            initialDate = LocalDate.now(),
            title = "Select date",
        ){
            selectedDate ->
            pickedDate = selectedDate
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDatePicker(){
    MaterialTheme{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val dateDialogState = rememberMaterialDialogState()

            MyDatePicker(
                dateDialogState = dateDialogState,
                positiveButton = {},
                negativeButton = {}
            )
        }
    }
}