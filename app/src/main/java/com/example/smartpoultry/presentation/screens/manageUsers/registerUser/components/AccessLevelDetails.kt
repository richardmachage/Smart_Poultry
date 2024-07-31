package com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.AccessLevelItem

@Preview(showSystemUi = true)
@Composable
fun AccessLevelDetails(
    onResponse : (AccessLevelDetailsResponse) -> Unit = {},
    accessLevelDetailsResponse: AccessLevelDetailsResponse = AccessLevelDetailsResponse()
){
    var accessResponse by remember{ mutableStateOf(accessLevelDetailsResponse) }

    Box (
        modifier = Modifier.fillMaxWidth()
    ){
        Column {


            AccessLevelItem(
                itemName = stringResource(id = R.string.level_egg_collection),//"Egg Collection",
                description = stringResource(id = R.string.egg_collection_description),
                isChecked = accessResponse.eggCollection,//accountViewModel.eggCollectionAccess.value,
                onCheckedChanged = {
                    // accountViewModel.eggCollectionAccess.value = it
                    accessResponse = accessResponse.copy(eggCollection = it)
                    onResponse(accessResponse)

                })

            AccessLevelItem(
                itemName = stringResource(id = R.string.level_edit_hen_count),//"Edit Hen Count",
                description = stringResource(id = R.string.edit_hen_count_description),
                isChecked = accessResponse.editHenCount,//accountViewModel.editHenCountAccess.value,
                onCheckedChanged = {
                    // accountViewModel.editHenCountAccess.value = it
                    accessResponse = accessResponse.copy(editHenCount = it)
                    onResponse(accessResponse)
                })

            AccessLevelItem(
                itemName = stringResource(id = R.string.level_manage_blocks_cells),//"Manage Blocks & Cells",
                description = stringResource(id = R.string.manage_blocks_cells_description),//"Allows the user to add, delete or rename a cell or a block.",
                isChecked = accessResponse.manageBlocksCells,//accountViewModel.manageBlocksCellsAccess.value,
                onCheckedChanged = {
                    //accountViewModel.manageBlocksCellsAccess.value = it
                    accessResponse = accessResponse.copy(manageBlocksCells = it)
                    onResponse(accessResponse)
                })

            AccessLevelItem(
                itemName = stringResource(id = R.string.level_manage_users),//"Manage other users",
                description = stringResource(id = R.string.manage_users_description),//"This will allow the user to be able to register new users to the farm, delete other user accounts and also be able to change the access level of the other users",
                isChecked = accessResponse.manageUsers,//accountViewModel.manageUsersAccess.value,
                onCheckedChanged = {
                    //accountViewModel.manageUsersAccess.value = it
                    accessResponse = accessResponse.copy(manageUsers = it)
                    onResponse(accessResponse)
                })
        }
    }
}

data class AccessLevelDetailsResponse(
    var eggCollection: Boolean = false,
    var editHenCount: Boolean = false,
    var manageBlocksCells: Boolean = false,
    var manageUsers: Boolean = false,
)