package com.example.smartpoultry.presentation.screens.accountScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.AccessLevelItem
import com.example.smartpoultry.presentation.composables.MyBorderedColumn
import com.example.smartpoultry.presentation.composables.MyCircularProgressBar
import com.example.smartpoultry.presentation.composables.MyEditTextClear
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.UserTypeDropDownMenu
import com.example.smartpoultry.presentation.destinations.ManageUsersScreenDestination
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
    val userRole by remember {
        mutableStateOf(accountViewModel.user.role)
    }//accountViewModel.userRole.collectAsState()
    val userName by remember {
        mutableStateOf(accountViewModel.user.name)
    }//accountViewModel.userName.collectAsState()
    val userEmail by remember {
        mutableStateOf(accountViewModel.user.email)
    }//accountViewModel.userEmail.collectAsState()
    val userPhone by remember {
        mutableStateOf(accountViewModel.user.phone)
    }//accountViewModel.userPhone.collectAsState()

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
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // AccountCard()
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "My Account",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        UserInfoRow(
                            ImageVector.vectorResource(id = R.drawable.verified_user),
                            label = "Role",
                            value = userRole
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        //Name
                        var showNameDialog by remember { mutableStateOf(false) }
                        var newUserName by remember { mutableStateOf(userName) }
                        MyInputDialog(
                            showDialog = showNameDialog,
                            title = "User Name",
                            onConfirm = {
                                if (newUserName.isBlank())
                                    accountViewModel.toastMessage.value = "Empty field"
                                else if (newUserName == userName)
                                    accountViewModel.toastMessage.value =
                                        "Same name, no change made"
                                else {
                                    accountViewModel.changeUserName(newUserName)
                                    showNameDialog = false
                                }
                            },
                            onDismiss = { showNameDialog = false }
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
                        UserInfoRow(
                            Icons.Default.Person,
                            "name",
                            value = userName.ifBlank { "No name set" },
                            true,
                            onEditClick = {
                                showNameDialog = true
                            })

                        Spacer(modifier = Modifier.height(16.dp))

                        //Email address
                        var showEmailDialog by remember { mutableStateOf(false) }
                        var newEmail by remember { mutableStateOf(userEmail) }
                        MyInputDialog(
                            showDialog = showEmailDialog,
                            title = "Email",
                            onConfirm = {
                                if (newEmail.isBlank())
                                    accountViewModel.toastMessage.value = "empty field"
                                else if (newEmail == userEmail) accountViewModel.toastMessage.value =
                                    "similar email, no change"
                                else {
                                    accountViewModel.changeEmail(email = newEmail)
                                    showEmailDialog = false
                                }
                            },
                            onDismiss = { showEmailDialog = false }
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
                        UserInfoRow(
                            Icons.Default.Email,
                            "Email address",
                            userEmail,
                            true,
                            onEditClick = { showEmailDialog = true })
                        Spacer(modifier = Modifier.height(16.dp))

                        //phone number
                        var showPhoneDialog by remember { mutableStateOf(false) }
                        var newPhone by remember { mutableStateOf(userPhone) }
                        MyInputDialog(
                            showDialog = showPhoneDialog,
                            title = "Phone Number",
                            onConfirm = {
                                if (newPhone.isBlank()) accountViewModel.toastMessage.value =
                                    "empty field"
                                else if (newPhone == userPhone) accountViewModel.toastMessage.value =
                                    "same phone number, no change made"
                                else {
                                    accountViewModel.changePhoneNumber(phoneNumber = newPhone)
                                    showPhoneDialog = false
                                }
                            },
                            onDismiss = { showPhoneDialog = false }
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
                        UserInfoRow(
                            Icons.Default.Phone,
                            "Phone Number",
                            userPhone.ifBlank { "No number set" },
                            true,
                            onEditClick = { showPhoneDialog = true })
                    }
                }
                //}
                //Register new user
                var showRegDialog by remember { mutableStateOf(false) }
                var userEmailReg by remember { mutableStateOf("") }
                var userRoleReg by remember { mutableStateOf("") }
                var userNameReg by remember { mutableStateOf("") }
                var userPhoneReg by remember { mutableStateOf("") }
                MyInputDialog(
                    showDialog = showRegDialog,
                    title = "Register New User",
                    onConfirm = {
                        //TODO -> validate Email address, and role selected.
                        accountViewModel.registerUser(email = userEmailReg.trim(), userRole = userRoleReg, name = userNameReg.trim(), phone = userPhoneReg.trim())
                        showRegDialog = false
                    },
                    onDismiss = { showRegDialog = false }
                ) {
                    Column {

                        UserTypeDropDownMenu(onItemClick = { userRole ->
                            userRoleReg = userRole
                        })

                        var expanded by remember { mutableStateOf(false) }

                        MyBorderedColumn {
                            Row (modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween){
                                Text(text = "Specify user access Level")
                                IconButton(onClick = { expanded = !expanded}) {
                                    Icon(
                                        Icons.Filled.ArrowDropDown,
                                        null,
                                        Modifier.rotate(if (expanded) 180f else 0f)
                                    )
                                }

                            }
                            if (expanded){
                                AccessLevelItem(itemName = "Egg Collection", description = "Allows the user to be input to eggs collection records", isChecked = accountViewModel.eggCollectionAccess.value, onChecked = {accountViewModel.eggCollectionAccess.value = it } )
                                AccessLevelItem(itemName = "Edit Hen Count", description = "Allows the user to be edit the number of hens in a cell ", isChecked = accountViewModel.editHenCountAccess.value, onChecked = { accountViewModel.editHenCountAccess.value = it} )
                                AccessLevelItem(itemName = "Manage Blocks & Cells", description = "Allows the user to add, delete or rename a cell or a block.", isChecked =  accountViewModel.manageBlocksCellsAccess.value, onChecked = { accountViewModel.manageBlocksCellsAccess.value = it} )
                                AccessLevelItem(itemName = "Manage other users", description = "This will allow the user to be able to register new users to the farm, delete other user accounts and also be able to change the access level of the other users", isChecked =  accountViewModel.manageUsersAccess.value, onChecked = { accountViewModel.manageUsersAccess.value = it} )
                                }
                        }

                        MyEditTextClear( // email input
                            label = "Email address",
                            iconLeading = Icons.Default.Email,
                            iconLeadingDescription = "Email",
                            keyboardType = KeyboardType.Email,
                            onValueChange = { text ->
                                userEmailReg = text.trim()
                            }
                        )
                        MyEditTextClear(// input name
                            label = "User Name",
                            iconLeading = Icons.Default.Person,
                            iconLeadingDescription = "person",
                            keyboardType = KeyboardType.Text,
                            onValueChange = { text ->
                                userEmailReg = text.trim()
                            }
                        )
                        MyEditTextClear( // input phone
                            label = "Phone number",
                            iconLeading = Icons.Default.Phone,
                            iconLeadingDescription = "Email",
                            keyboardType = KeyboardType.Phone,
                            onValueChange = { text ->
                                userEmailReg = text.trim()
                            }
                        )
                    }
                }
                if (userRole == "Director" || userRole == "Super") {
                    Spacer(modifier = Modifier.height(24.dp))
                    ActionButton(
                        text = "Register a new user",
                        icon = ImageVector.vectorResource(id = R.drawable.person_add),
                        onClick = { showRegDialog = true })
                    Spacer(modifier = Modifier.height(16.dp))
                    ActionButton(
                        "Manage existing users",
                        ImageVector.vectorResource(id = R.drawable.supervisor_account),
                        onClick = {
                            navigator.navigate(
                                ManageUsersScreenDestination
                            )
                        })


                }
            }

        }
    }

}


@Composable
fun UserInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    editable: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                fontSize = 16.sp
            )
        }
        if (editable) {
            IconButton(onClick = { onEditClick() }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
        //  .height(48.dp)
        ,
        shape = RoundedCornerShape(size = 24.dp)//RoundedCornerShape(24.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}
