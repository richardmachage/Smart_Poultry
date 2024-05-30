package com.example.smartpoultry.presentation.screens.manageUsers

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUsersBottomSheet(
    //showBottomSheet: Boolean,
    user: User,
    sheetState: SheetState,
    scope: CoroutineScope,
    onDismiss : () -> Unit = {}
){
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible){
                    onDismiss() //set showBottomSheet = false
                }
            }
        }) {
            Text(text = "hide bottom sheet")
        }
    }
}