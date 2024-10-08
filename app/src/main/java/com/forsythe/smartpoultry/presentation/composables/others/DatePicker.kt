package com.forsythe.smartpoultry.presentation.composables.others

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.forsythe.smartpoultry.utils.localDateToJavaDate
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.SimpleDateFormat
import java.time.LocalDate


@SuppressLint("NewApi", "SimpleDateFormat")
@Composable

@RequiresApi(Build.VERSION_CODES.O)
fun MyDatePicker(
    modifier: Modifier = Modifier,
    dateDialogState: MaterialDialogState,
    label: String,
    positiveButtonOnClick: (LocalDate) -> Unit,
    allowedDateValidate : (LocalDate) -> Boolean = {true},
    negativeButton: () -> Unit = {},
) {

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    OutlinedTextField(
        modifier = modifier
            //.fillMaxWidth()
            .padding(4.dp),
        value = SimpleDateFormat("dd/MM/yyyy")
            .format(
                localDateToJavaDate(pickedDate)
            ),
        label = { Text(text = label) },
//        placeholder = { Text(text = label) },
        onValueChange = { },
        trailingIcon = {
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

        },
        singleLine = true,
        readOnly = true
    )


    MaterialDialog( // defines a material dialog
        dialogState = dateDialogState,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
        buttons = {
            positiveButton(text = "OK") {
                //TODO what happens after ok is clicked?
                positiveButtonOnClick(pickedDate)
            }

            negativeButton(text = "Cancel") {
                negativeButton()
            }

        }
    ) { // now here we specify the kind of material dialog such as date picker
        datepicker(
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.onPrimary,
                headerTextColor = MaterialTheme.colorScheme.primary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,
                dateActiveTextColor = MaterialTheme.colorScheme.primary,
                ),
            initialDate = LocalDate.now(),
            title = "Select date",
            allowedDateValidator = {
                allowedDateValidate(it)
            }
        ) { selectedDate ->
            pickedDate = selectedDate
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDatePicker() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Column {
                val dateDialogState = rememberMaterialDialogState()

                MyDatePicker(
                    dateDialogState = dateDialogState,
                    label = "start date",
                    positiveButtonOnClick = {},
                    negativeButton = {}
                )
            }

        }
    }
}