package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RadioButtonGroup(
    listOfOptions : List<String>,
    onOptionSelect : (String) -> Unit = {}
) {
    //val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf(listOfOptions.first()) }

    Row {
        listOfOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.selectable(
                    selected = (option == selectedOption),
                    onClick = {
                        selectedOption = option
                    }
                )) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        selectedOption = option
                        onOptionSelect(option)

                    }
                )
                Text(text = option)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RadioButtonPrev() {
    RadioButtonGroup(listOf("Option 1", "Option 2", "Option 3"))
}