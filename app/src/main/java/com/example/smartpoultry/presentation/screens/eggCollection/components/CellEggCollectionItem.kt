package com.example.smartpoultry.presentation.screens.eggCollection.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.MyCard
import com.example.smartpoultry.presentation.composables.MyOutlineButton
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer

@Preview
@Composable
fun CellEggCollectionItem(
    modifier: Modifier = Modifier,
    cellNum: Int = 0,
    henCount: Int = 0,
    onSave: (Int) -> Unit = {}
) {
    var eggCount by remember { mutableStateOf(TextFieldValue("0")) }
    var hasError by remember { mutableStateOf(false) }


    MyCard(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(text = "Cell : $cellNum", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Chicken : $henCount", style = MaterialTheme.typography.bodyLarge)
                }

                MyVerticalSpacer(height = 5)

                OutlinedTextField(
                    modifier = Modifier
                        .padding(6.dp)
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            if (focusState.hasFocus && eggCount.text == "0") {
                                eggCount = TextFieldValue("", TextRange(0))
                            } else if (!focusState.hasFocus && eggCount.text == "") {
                                eggCount = TextFieldValue("0")
                            }
                        },
                    value = eggCount,
                    onValueChange = { newText ->
                        // val text = it.toIntOrNull()?:0
                        eggCount = newText.copy(
                            text = newText.text,
                            selection = TextRange(newText.text.length)
                        )
                        hasError = (eggCount.text.toIntOrNull() ?: 0) > henCount
                    },
                    label = { Text(text = stringResource(id = R.string.input_egg_count)) },
                    isError = hasError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.egg_outline),
                            contentDescription = "egg"
                        )
                    },
                    singleLine = true,
                    placeholder = { Text(text = "0") },
                    supportingText = {
                        AnimatedVisibility(visible = hasError) {
                            Text(text = "Number of eggs can't exceed chicken")
                        }
                    }
                )


                MyOutlineButton(
                    modifier = Modifier.weight(1f),
                    onButtonClick = { onSave(eggCount.text.toIntOrNull() ?: 0) },
                    btnName = stringResource(id = R.string.save_btn),
                    enabled = !hasError
                )
            }
        }
    }
}