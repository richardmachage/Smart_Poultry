package com.example.smartpoultry.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyInputDialog(
    showDialog: Boolean = false,
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
                    btnName = "Ok",
                    modifier = Modifier
                )
            },
            dismissButton = {
                NormButton(
                    onButtonClick = onDismiss,
                    btnName = "Cancel",
                    modifier = Modifier
                )
            }

        )
    }
}