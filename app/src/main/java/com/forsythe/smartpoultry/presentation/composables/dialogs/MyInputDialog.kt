package com.forsythe.smartpoultry.presentation.composables.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.presentation.composables.buttons.MyOutlineButton
import com.forsythe.smartpoultry.presentation.composables.buttons.NormButton

@Composable
fun MyInputDialog(
    showDialog: Boolean = false,
    confirmBtnName : String = stringResource(id = R.string.ok_btn),
    title: String,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit,
    dialogContent: @Composable () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = dialogContent,
            confirmButton = {
                NormButton(
                    onButtonClick = onConfirm,
                    btnName = confirmBtnName,
                    modifier = Modifier
                )
            },
            dismissButton = {
                MyOutlineButton(
                    onButtonClick = onDismiss,
                    btnName = stringResource(id = R.string.cancel_btn),
                    modifier = Modifier
                )
            }

        )
    }
}