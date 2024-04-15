package com.example.smartpoultry.presentation.screens.accountScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.presentation.composables.MyBorderedColumn
import com.example.smartpoultry.presentation.composables.MyCircularProgressBar
import com.example.smartpoultry.presentation.composables.MyEditText
import com.example.smartpoultry.presentation.composables.MyEditTextClear
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.UserTypeDropDownMenu
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AccountScreen(
    navigator: DestinationsNavigator
) {
    val accountViewModel = hiltViewModel<AccountViewModel>()
    val context = LocalContext.current
    val userRole by accountViewModel.userRole.collectAsState()
    val userName by accountViewModel.userName.collectAsState()
    val userEmail by accountViewModel.userEmail.collectAsState()
    val userPhone by accountViewModel.userPhone.collectAsState()
    var refreshData = 0

    LaunchedEffect(key1 = accountViewModel.toastMessage.value) {
        if (accountViewModel.toastMessage.value.isNotBlank()) Toast.makeText(
            context,
            accountViewModel.toastMessage.value,
            Toast.LENGTH_SHORT
        ).show()
        accountViewModel.toastMessage.value = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Account Details") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {

            MyCircularProgressBar(isLoading = accountViewModel.isLoading.value)

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {

                MyBorderedColumn(
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text(text = "My Account")

                    Row(// User Role
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var showDialog by remember { mutableStateOf(false) }
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "User Role",
                            onConfirm = { showDialog = false },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Role",
                                keyboardType = KeyboardType.Email,
                                initialText = "",
                                onValueChange = {
                                    //newThreshold = it
                                }
                            )
                        }

                        MyEditText(
                            value = userRole,
                            label = "User Role",
                            iconLeading = Icons.Default.Face,
                            iconLeadingDescription = "userRole",
                            enabled = true,
                            readOnly = true
                        )
                        /*if (userRole == "Director") {
                            IconButton(onClick = {
                                showDialog = true
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                            }
                        }*/
                    }


                    Row(//User Name
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var showDialog by remember { mutableStateOf(false) }
                        var newUserName by remember { mutableStateOf(userName) }
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "User Name",
                            onConfirm = {
                                if (newUserName.isBlank())
                                    accountViewModel.toastMessage.value = "Empty field"
                                else if (newUserName == userName)
                                    accountViewModel.toastMessage.value =
                                        "Same name, no change made"
                                else {
                                    accountViewModel.changeUserName(newUserName)
                                    showDialog = false
                                }
                            },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "User Name",
                                keyboardType = KeyboardType.Text,
                                initialText = newUserName,
                                onValueChange = {
                                    newUserName = it
                                }
                            )
                        }

                        MyEditText(
                            value = userName,
                            label = "User name",
                            iconLeading = Icons.Default.Person,
                            iconLeadingDescription = "user",
                            enabled = true,
                            readOnly = true
                        )
                        IconButton(
                            onClick = {
                                showDialog = true
                            }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                        }
                    }



                    Row(// Email address
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var showDialog by remember { mutableStateOf(false) }
                        var newEmail by remember { mutableStateOf(userEmail) }
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "Email",
                            onConfirm = {
                                if (newEmail.isBlank())
                                    accountViewModel.toastMessage.value = "empty field"
                                else if (newEmail == userEmail) accountViewModel.toastMessage.value =
                                    "similar email, no change"
                                else {
                                    accountViewModel.changeEmail(email = newEmail)
                                    showDialog = false
                                }
                            },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Email",
                                keyboardType = KeyboardType.Email,
                                initialText = newEmail,
                                onValueChange = {
                                    newEmail = it
                                }
                            )
                        }

                        MyEditText(
                            value = userEmail,
                            label = "Email address",
                            iconLeading = Icons.Default.Email,
                            iconLeadingDescription = "Email",
                            enabled = true,
                            readOnly = true
                        )
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                        }
                    }

                    Row(// Phone Number
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var showDialog by remember { mutableStateOf(false) }
                        var newPhone by remember { mutableStateOf(userPhone) }
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "Phone Number",
                            onConfirm = {
                                if (newPhone.isBlank()) accountViewModel.toastMessage.value =
                                    "empty field"
                                else if (newPhone == userPhone) accountViewModel.toastMessage.value =
                                    "same phone number, no change made"
                                else {
                                    accountViewModel.changePhoneNumber(phoneNumber = newPhone)
                                    showDialog = false
                                }
                            },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Phone Number",
                                keyboardType = KeyboardType.Email,
                                initialText = newPhone,
                                onValueChange = {
                                    newPhone = it
                                }
                            )
                        }

                        MyEditText(
                            value = userPhone,
                            label = "Phone Number",
                            iconLeading = Icons.Default.Phone,
                            iconLeadingDescription = "Phone",
                            readOnly = true,
                            enabled = true
                        )
                        /*Icon(imageVector = Icons.Default.Phone, contentDescription = "edit")
                        Text(text = "Phone Number : 0654233214")*/
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                        }
                    }
                    MyVerticalSpacer(height = 10)



                }

                //Register new user
                var showRegDialog by remember { mutableStateOf(false) }
                MyInputDialog(
                    showDialog = showRegDialog,
                    title = "Register New User",
                    onConfirm = {
                        //TODO
                        showRegDialog = false
                    },
                    onDismiss = { showRegDialog = false }
                ) {
                    Column {

                        UserTypeDropDownMenu(onItemClick = { userRole -> })
                        MyEditTextClear(
                            label = "Email address",
                            iconLeading = Icons.Default.Email,
                            iconLeadingDescription = "Email",
                            keyboardType = KeyboardType.Email,
                            onValueChange = { text -> }
                        )
                    }
                }
                if (userRole == "Director") {
                    NormButton(
                        modifier = Modifier.fillMaxWidth(),
                        onButtonClick = { showRegDialog = true },
                        btnName = "Register new user"
                    )
                }
            }

        }
    }

}