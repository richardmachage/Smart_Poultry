package com.forsythe.smartpoultry.presentation.screens.manageUsers

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.User
import com.forsythe.smartpoultry.presentation.composables.buttons.MyFloatingActionButton
import com.forsythe.smartpoultry.presentation.composables.dialogs.InfoDialog
import com.forsythe.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.forsythe.smartpoultry.presentation.composables.spacers.MyHorizontalSpacer
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.destinations.AccountScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.RegisterUserScreenDestination
import com.forsythe.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ManageUsersScreen(
    navigator: DestinationsNavigator
) {
    val manageUsersViewModel: ManageUsersViewModel = hiltViewModel()
    val context = LocalContext.current
    var showBottomSheet by remember { manageUsersViewModel.showBottomSheet }

    LaunchedEffect(manageUsersViewModel.toastMessage.value) {
        if (manageUsersViewModel.toastMessage.value.isNotBlank()) {
            Toast.makeText(context, manageUsersViewModel.toastMessage.value, Toast.LENGTH_SHORT)
                .show()
            manageUsersViewModel.toastMessage.value = ""
        }
    }

    LaunchedEffect(manageUsersViewModel.updateListOfEmployees.value) {
        if (manageUsersViewModel.updateListOfEmployees.value.isNotBlank()) {
            manageUsersViewModel.updateListOfUsers()
            manageUsersViewModel.updateListOfEmployees.value = ""
        }
    }
    SmartPoultryTheme {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Manage Users") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator.navigate(AccountScreenDestination) {
                                popUpTo(AccountScreenDestination) { inclusive = true }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                    }
                )

            },
            floatingActionButton = {
                var showUpgradeDialog by remember { mutableStateOf(false) }
                InfoDialog(
                    showDialog = showUpgradeDialog,
                    title = stringResource(R.string.upgrade_to_premium),
                    message = stringResource(R.string.upgrade_to_premium_message),
                    onConfirm = {
                        showUpgradeDialog = false
                    }
                )
                Column {

                    MyFloatingActionButton(
                        onClick = {
                            manageUsersViewModel.refreshList()
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Refresh,//ImageVector.vectorResource(id = R.drawable.person_add),
                                contentDescription = "refresh"
                            )
                        },
                        text = {
                            Text(
                                text = "Refresh",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                    MyVerticalSpacer(height = 15)
                    MyFloatingActionButton(
                        onClick = {
                            //check if theres already two other users
                            //TODO add a check for subscription status as well in the condition
                            if (manageUsersViewModel.listOfUsers.size < 3){
                                navigator.navigate(RegisterUserScreenDestination)
                            }
                            else {
                                showUpgradeDialog = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.person_add),
                                contentDescription = "add_user"
                            )
                        },
                        text = {
                            Text(
                                text = "Add User",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
            }
        ) {
            MyCircularProgressBar(isLoading = manageUsersViewModel.isLoading.value)
            if (showBottomSheet) {
                ManageUsersBottomSheet(
                    user = manageUsersViewModel.selectedUser!!,
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    scope = rememberCoroutineScope(),
                    onDismiss = { user, accessLevel, doUpdate ->
                        if (doUpdate) {
                            manageUsersViewModel.viewModelScope.launch {
                                if (manageUsersViewModel.editAccessLevel(
                                        user,
                                        accessLevel
                                    )
                                ) showBottomSheet = false else showBottomSheet = true
                            }
                        } else {
                            showBottomSheet = false
                        }
                    },
                    onDelete = { user ->
                        manageUsersViewModel.onDeleteUser(userId = user.userId)
                    },
                    accessLevel = manageUsersViewModel.currentAccessLevel!!
                )
            }

           // val listOfEmployees by remember { mutableStateOf(manageUsersViewModel.listOfUsers.toList()) }
            if (
                manageUsersViewModel.anyUsers.value
            ) {

                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    items(manageUsersViewModel.listOfUsers.filter { user -> user.email != manageUsersViewModel.getUserEmail() })
                    { user ->
                        UserListItem(
                            user = user,
                            onClick = { userClicked ->
                                manageUsersViewModel.onListItemClicked(userClicked)
                            }
                        )
                    }

                }
            } else {
                Box (modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp), contentAlignment = Alignment.Center){
                    Text(
                        fontStyle = FontStyle.Italic,
                        text = "There are no farm other users currently, \nyou can click on add user below to add one.. \nor click on refresh to load the list again")
                }
            }
        }
    }
}


@Composable
fun UserListItem(
    user: User,
    onClick: (User) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(start = 6.dp, end = 6.dp)
            .clickable {
                onClick(user)
            }
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceDim,
                        MaterialTheme.colorScheme.surfaceBright
                    )
                )
            )
            .padding(16.dp)

    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = if (user.firstName.isNotBlank()) user.firstName.first().uppercaseChar()
                    .toString() else user.email.first().uppercase(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        MyHorizontalSpacer(width = 16)
        // Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row {
                if (user.firstName.isNotBlank()) {
                    Text(
                        text = user.firstName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        fontSize = 20.sp
                    )
                }
                MyHorizontalSpacer(width = 5)
                if (user.lastName.isNotBlank()) {
                    Text(
                        text = user.lastName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        fontSize = 20.sp
                    )
                }
            }

            MyVerticalSpacer(height = 5)
            if (user.phone.isNotBlank()) {
                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp
                )
            }
            MyVerticalSpacer(height = 2)
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp
            )
        }
    }
}

