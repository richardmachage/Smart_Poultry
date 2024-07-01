package com.example.smartpoultry.presentation.screens.manageUsers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
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
import com.example.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.screens.accountScreen.UserInfoRow
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
    onDismiss: () -> Unit = {},
    onDelete: (User) -> Unit = {}
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismiss() //set showBottomSheet = false
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( //Tittle
                text = "Edit Details",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            //userName
            MyVerticalSpacer(height = 20)
            if (user.name.isNotBlank()) {
                UserInfoRow(Icons.Default.Person, "name", value = user.name, false)
            }

            //user email
            MyVerticalSpacer(height = 10)
            UserInfoRow(Icons.Default.Email, "Email", value = user.email, false)

            //userPhone
            if (user.phone.isNotBlank()) {
                UserInfoRow(Icons.Default.Phone, "Phone", value = user.phone, false)
            }

            //user role
            /*var shoeEditRole by remember { mutableStateOf(false) }
            UserInfoRow(
                icon = ImageVector.vectorResource(id = R.drawable.verified_user),
                label = "Role",
                value = user.role,
                editable = true,
                onEditClick = { shoeEditRole = !shoeEditRole }
            )

            if (shoeEditRole) {
                UserTypeDropDownMenu(onItemClick = { shoeEditRole = false })
            }*/

            MyVerticalSpacer(height = 10)

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
                    .padding(10.dp),
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