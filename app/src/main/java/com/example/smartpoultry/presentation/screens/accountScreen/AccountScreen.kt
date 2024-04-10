package com.example.smartpoultry.presentation.screens.accountScreen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.datastore.USER_ROLE_KEY
import com.example.smartpoultry.presentation.composables.MyBorderedColumn
import com.example.smartpoultry.presentation.composables.MyEditText
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.NormButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AccountScreen(
    navigator: DestinationsNavigator
) {
    val accountViewModel = hiltViewModel<AccountViewModel>()
    val userRole = accountViewModel.myDataStore.readData(USER_ROLE_KEY).collectAsState(initial = "")

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
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {

                MyBorderedColumn (
                    modifier = Modifier.padding(6.dp)
                ){
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
                        /*Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "role")
                        Text(text = "Role : ${userRole.value}")*/

                        MyEditText(
                            value = userRole.value,
                            label = "User Role",
                            iconLeading = Icons.Default.Face,
                            iconLeadingDescription = "userRole",
                            enabled = false
                        )

                        if (userRole.value == "Director"){
                            IconButton(onClick = {
                                showDialog = true
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                            }
                        }

                    }


                    Row(//User Name
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var showDialog by remember { mutableStateOf(false) }
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "User Name",
                            onConfirm = { showDialog = false },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "User Name",
                                keyboardType = KeyboardType.Text,
                                initialText = "",
                                onValueChange = {
                                    //newThreshold = it
                                }
                            )
                        }

                     /*   Icon(imageVector = Icons.Default.Person, contentDescription = "edit")
                        Text(text = "User Name : Beast", textAlign = TextAlign.Start)*/
                        MyEditText(
                            value = "Beast Mode",
                            label = "User name",
                            iconLeading = Icons.Default.Person,
                            iconLeadingDescription = "user",
                            enabled = false
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
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "Email",
                            onConfirm = { showDialog = false },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Email",
                                keyboardType = KeyboardType.Email,
                                initialText = "",
                                onValueChange = {
                                    //newThreshold = it
                                }
                            )
                        }

                        /*Icon(imageVector = Icons.Default.Email, contentDescription = "email")
                        Text(text = "Email Address: beast@gmail.com")*/
                        MyEditText(
                            value = "beast.com.ku@gmail.com",
                            label = "Email address",
                            iconLeading = Icons.Default.Email,
                            iconLeadingDescription ="Email",
                            enabled = false
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
                        MyInputDialog(
                            showDialog = showDialog,
                            title = "Phone Number",
                            onConfirm = { showDialog = false },
                            onDismiss = { showDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Phone Number",
                                keyboardType = KeyboardType.Email,
                                initialText = "",
                                onValueChange = {
                                    //newThreshold = it
                                }
                            )
                        }

                        MyEditText(
                            value = "0654233214",
                            label ="Phone Number",
                            iconLeading =  Icons.Default.Phone,
                            iconLeadingDescription = "Phone",
                            readOnly = true,
                            enabled = false
                        )
                        /*Icon(imageVector = Icons.Default.Phone, contentDescription = "edit")
                        Text(text = "Phone Number : 0654233214")*/
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                        }
                    }
                }

                var showRegDialog by remember { mutableStateOf(false) }
                MyInputDialog(
                    showDialog = showRegDialog,
                    title = "Register New User",
                    onConfirm = {
                                //TODO
                                showRegDialog = false
                    },
                    onDismiss = { showRegDialog = false}
                ) {
                    Column {

                        MyOutlineTextFiled(
                            label = "Name",
                            keyboardType = KeyboardType.Text,
                            initialText = "",
                            onValueChange = {}
                        )
                        MyOutlineTextFiled(
                            label = "Email address",
                            keyboardType = KeyboardType.Email,
                            initialText = "",
                            onValueChange = {}
                        )
                    }
                }
                if (userRole.value == "Director") {
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