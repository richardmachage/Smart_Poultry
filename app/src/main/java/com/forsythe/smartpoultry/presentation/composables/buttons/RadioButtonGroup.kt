package com.forsythe.smartpoultry.presentation.composables.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer

@Composable
fun RadioButtonGroup(
    modifier: Modifier = Modifier,
    title : String,
    listOfOptions: List<String>,
    onOptionSelect: (String) -> Unit = {}
) {
    //val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf(listOfOptions.first()) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(
                    (0.009 * LocalConfiguration.current.screenWidthDp).dp
                )
            )
            .padding(6.dp)
    ) {

        Text(text = title)
        MyVerticalSpacer(height = 5)
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOfOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = (option == selectedOption),
                            onClick = {
                                selectedOption = option
                               // onOptionSelect(option)
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RadioButtonPrev() {
    RadioButtonGroup(
        title = "Select type of analysis:",
        listOfOptions = listOf("Past \nX Days", "Custom \nRange", "Monthly"))
}