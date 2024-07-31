package com.example.smartpoultry.presentation.screens.manageUsers

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.R
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.presentation.composables.MyCircularProgressBar
import com.example.smartpoultry.presentation.composables.MyHorizontalSpacer
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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
                            navigator.navigateUp()
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
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    elevation = FloatingActionButtonDefaults.elevation(20.dp)
                ) {
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.person_add), contentDescription = null)
                }
            }
        ) {
            MyCircularProgressBar(isLoading = manageUsersViewModel.isLoading.value)
            if (showBottomSheet) {
                ManageUsersBottomSheet(
                    user = manageUsersViewModel.selectedUser!!,
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    scope = rememberCoroutineScope(),
                    onDismiss = {user, accessLevel, doUpdate->
                        if (doUpdate){
                            manageUsersViewModel.viewModelScope.launch{
                                if(manageUsersViewModel.editAccessLevel(user,accessLevel)) showBottomSheet = false  else showBottomSheet = true
                            }
                        }
                        else {
                            showBottomSheet = false
                        }
                    },
                    onDelete = { user ->
                        manageUsersViewModel.onDeleteUser(userId = user.userId)
                    },
                    accessLevel = manageUsersViewModel.currentAccessLevel!!
                )
            }

            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .animateContentSize()
            ) {

                items(manageUsersViewModel.listOfUsers.filter { user -> user.email != manageUsersViewModel.getUserEmail() })
                { user ->
                    // UserItem()
                    //Text(text = "userId : ${user.userId} != ${manageUsersViewModel.myId}")
                    UserListItem(
                        user = user,
                        onClick = { userClicked ->
                            manageUsersViewModel.onListItemClicked(userClicked)
                        }
                    )
                    //Text(text = "users: ${manageUsersViewModel.listOfUsers.size}")

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
            // .padding(start = 6.dp, end = 6.dp)
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

