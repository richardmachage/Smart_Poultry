package com.example.smartpoultry.presentation.composables.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.text.TitleText

@Composable
fun InfoDialog(
    showDialog: Boolean,//= false,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    icon : @Composable (()-> Unit)? = null
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {  },
            title = {
                TitleText(text = title)
            },
            text = {
                Box {
                    Text(text = message)
                }
            },
            icon = icon,
            confirmButton = {
                NormButton(
                    onButtonClick = { onConfirm() },
                    btnName = stringResource(id = R.string.dismiss_dialog_btn)
                )
            }
        )
    }
}