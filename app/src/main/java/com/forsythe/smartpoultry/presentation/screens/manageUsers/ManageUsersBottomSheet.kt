package com.forsythe.smartpoultry.presentation.screens.manageUsers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.User
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.others.AccessLevelItem
import com.forsythe.smartpoultry.presentation.composables.others.MyBorderedColumn
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.screens.accountScreen.UserInfoRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUsersBottomSheet(
    //showBottomSheet: Boolean,
    user: User,
    accessLevel: AccessLevel,
    sheetState: SheetState,
    scope: CoroutineScope,
    onDismiss: (User,AccessLevel, Boolean) -> Unit,
    onDelete: (User) -> Unit = {}
) {
   // var doUpdate by remember { mutableStateOf(false) }
    var eggCollectionAccess by remember { mutableStateOf(accessLevel.collectEggs) }
    var editHenCountAccess by remember { mutableStateOf(accessLevel.editHenCount) }
    var manageUsersAccess by remember { mutableStateOf(accessLevel.manageUsers) }
    var manageBlocksAccess by remember { mutableStateOf(accessLevel.manageBlocksCells) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    if (
                        accessLevel.manageUsers != manageUsersAccess ||
                        accessLevel.editHenCount != editHenCountAccess ||
                        accessLevel.manageBlocksCells != manageBlocksAccess ||
                        accessLevel.collectEggs != eggCollectionAccess
                    ){
                        onDismiss(
                            user,
                            AccessLevel(collectEggs = eggCollectionAccess, editHenCount = editHenCountAccess, manageUsers = manageUsersAccess, manageBlocksCells = manageBlocksAccess),
                            true)
                    }else{
                        onDismiss(user,accessLevel,false)
                    }
                    //onDismiss(user,accessLevel, doUpdate) //set showBottomSheet = false
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( //Tittle
                text = "More Details",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )

            //userName
            MyVerticalSpacer(height = 10)
            if (user.firstName.isNotBlank()) {
                UserInfoRow(Icons.Default.Person, "name", value = user.firstName + " "+ user.lastName, false)
            }

            //user Gender
            MyVerticalSpacer(height = 10)
            UserInfoRow(icon = Icons.Default.Face, label = "gender", value =user.gender, false )

            //user email
            MyVerticalSpacer(height = 10)
            UserInfoRow(Icons.Default.Email, "Email", value = user.email, false)

            //userPhone
            MyVerticalSpacer(height = 10)
            if (user.phone.isNotBlank()) {
                UserInfoRow(Icons.Default.Phone, "Phone", value = user.phone, false)
            }

            MyVerticalSpacer(height = 10)
            MyBorderedColumn(modifier = Modifier.padding(8.dp)) {
                Text(text = "Access Level", modifier = Modifier.padding(8.dp))
                    AccessLevelItem(itemName = "Egg Collection", description = "Allows the user to be able to input the daily eggs collection records", isChecked = eggCollectionAccess, onCheckedChanged = { eggCollectionAccess = it} )
                    AccessLevelItem(itemName = "Edit Hen Count", description = "Allows the user to be edit the number of hens in a cell ", isChecked = editHenCountAccess, onCheckedChanged = {editHenCountAccess = it} )
                    AccessLevelItem(itemName = "Manage Blocks & Cells", description = "Allows the user to add, delete or rename a cell or a block.", isChecked =  manageBlocksAccess, onCheckedChanged = { manageBlocksAccess= it } )
                    AccessLevelItem(itemName = "Manage other users", description = "This will allow the user to be able to register new users to the farm, delete other user accounts and also be able to change the access level of the other users", isChecked =  manageUsersAccess, onCheckedChanged = {manageUsersAccess = it } )
            }

            var showDeleteDialog by remember { mutableStateOf(false) }
            MyInputDialog(
                showDialog = showDeleteDialog,
                title = "Delete User",
                onConfirm = {
                    onDelete(user)
                    showDeleteDialog = false
                },
                onDismiss = { showDeleteDialog = false }
            ) {
                Text(text = "Are you sure you want o delete this user? email: ${user.email}")
            }

            Button(//Delete user button
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Red,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) { //deleteButton
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                )//modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Deleter User")
            }
        }
        MyVerticalSpacer(height = 30)
        /*Button(
            onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss() //set showBottomSheet = false
                    }
                }
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Cancel")
        }*/
    }
}