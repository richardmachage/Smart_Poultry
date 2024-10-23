package com.forsythe.smartpoultry.presentation.screens.accountScreen

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.textInputFields.MyOutlineTextFiled
import com.forsythe.smartpoultry.presentation.destinations.ManageUsersScreenDestination
import com.forsythe.smartpoultry.presentation.screens.mainActivity.MainActivity
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
    val activity = context as MainActivity
    var userName by remember {
        mutableStateOf(accountViewModel.user.firstName)
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
                            text = stringResource(id = R.string.my_Profile_title),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        //Name
                        var showNameDialog by remember { mutableStateOf(false) }
                        var newFirstName by remember { mutableStateOf(accountViewModel.user.firstName )}
                        var newLastName by remember { mutableStateOf(accountViewModel.user.lastName )}
                        MyInputDialog(
                            showDialog = showNameDialog,
                            title = stringResource(id = R.string.user_name_title),//"User Name",
                            onConfirm = {
                                if (newFirstName.isBlank() || newLastName.isBlank())
                                    accountViewModel.toastMessage.value = "Empty field"
                                else if (newFirstName == accountViewModel.user.firstName && newLastName == accountViewModel.user.lastName)
                                    accountViewModel.toastMessage.value =
                                        "Same name, no change made"
                                else {
                                    if (newFirstName != accountViewModel.user.firstName){
                                        accountViewModel.editFirstName(newFirstName)
                                    }
                                    if (newLastName != accountViewModel.user.lastName)
                                        accountViewModel.editLastName(newLastName)
                                    }
                                    showNameDialog = false
                            },
                            onDismiss = { showNameDialog = false }
                        ) {
                            Column {

                                MyOutlineTextFiled(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "First Name",
                                    keyboardType = KeyboardType.Text,
                                    initialText = newFirstName,
                                    onValueChange = {
                                        newFirstName = it
                                    }
                                )

                                MyOutlineTextFiled(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "Last Name",
                                    keyboardType = KeyboardType.Text,
                                    initialText = newLastName,
                                    onValueChange = {
                                        newLastName = it
                                    }
                                )
                            }
                        }

                        //Name
                        UserInfoRow(
                            Icons.Default.Person,
                            label = stringResource(id = R.string.name_label),
                            value = accountViewModel.user.firstName + " " + accountViewModel.user.lastName,
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
                            title = stringResource(id = R.string.email_address_title),//"Email",
                            onConfirm = {
                                if (newEmail.isBlank())
                                    accountViewModel.toastMessage.value =
                                        R.string.empty_field.toString() //"empty field"
                                else if (newEmail == userEmail) accountViewModel.toastMessage.value = R.string.similar_email_no_change.toString()
                                   // "similar email, no change"
                                else {
                                    accountViewModel.changeEmail(email = newEmail)
                                    showEmailDialog = false
                                }
                            },
                            onDismiss = { showEmailDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = stringResource(id = R.string.email_label),
                                keyboardType = KeyboardType.Email,
                                initialText = newEmail,
                                onValueChange = {
                                    newEmail = it
                                }
                            )
                        }
                        UserInfoRow(
                            Icons.Default.Email,
                            label = stringResource(id = R.string.email_address_title),//"Email address",
                            userEmail,
                            true,
                            onEditClick = { showEmailDialog = true })
                        Spacer(modifier = Modifier.height(16.dp))

                        //phone number
                        var showPhoneDialog by remember { mutableStateOf(false) }
                        var newPhone by remember { mutableStateOf(userPhone) }
                        MyInputDialog(
                            showDialog = showPhoneDialog,
                            title = stringResource(id = R.string.phone_number_title),//"Phone Number",
                            onConfirm = {
                                if (newPhone.isBlank()) accountViewModel.toastMessage.value = R.string.empty_field.toString()
                                else if (newPhone == userPhone) accountViewModel.toastMessage.value = R.string.similar_phone_no_change.toString()
                                else {
                                    accountViewModel.changePhoneNumber(phoneNumber = newPhone)
                                    showPhoneDialog = false
                                }
                            },
                            onDismiss = { showPhoneDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = stringResource(id = R.string.phone_number_title),//"Phone Number",
                                keyboardType = KeyboardType.Phone,
                                initialText = newPhone,
                                onValueChange = {
                                    newPhone = it
                                }
                            )
                        }
                        UserInfoRow(
                            Icons.Default.Phone,
                            label = stringResource(id = R.string.phone_number_title),//"Phone Number",
                            userPhone.ifBlank { stringResource(id = R.string.no_number_set)},
                            editable = true,
                            onEditClick = { showPhoneDialog = true })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Farm Card
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
                            text = stringResource(id = R.string.my_poultry_farm_title),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth()
                        )

                        MyVerticalSpacer(height = 10)


                        //FarmName
                        var showFarmDialog by remember { mutableStateOf(false) }
                        var newFarmName by remember { mutableStateOf(accountViewModel.getFarmName() )}

                        MyInputDialog(
                            showDialog = showFarmDialog,
                            title = stringResource(id = R.string.phone_number_title),//"Phone Number",
                            onConfirm = {
                                if (newFarmName.isBlank()) accountViewModel.toastMessage.value = R.string.empty_field.toString()
                                else if (newFarmName == userPhone) accountViewModel.toastMessage.value = R.string.similar_farm_no_change.toString()
                                else {
                                    accountViewModel.changeFarmName(farmName = newFarmName)
                                    showFarmDialog = false
                                }
                            },
                            onDismiss = { showFarmDialog = false }
                        ) {
                            MyOutlineTextFiled(
                                modifier = Modifier.fillMaxWidth(),
                                label = stringResource(id =R.string.farm_name),
                                keyboardType = KeyboardType.Phone,
                                initialText = newFarmName,
                                onValueChange = {
                                    newFarmName = it
                                }
                            )
                        }
                        UserInfoRow(
                            icon = ImageVector.vectorResource(id = R.drawable.egg_outline),

                            label = stringResource(id = R.string.farm_name),
                            value = accountViewModel.getFarmName(),
                            editable = true,

                            onEditClick = {
                                showFarmDialog = true
                            }
                        )
                        MyVerticalSpacer(height = 10)

                        //Country
                        UserInfoRow(
                            icon = Icons.Default.LocationOn,
                            label = stringResource(id = R.string.country),
                            value = accountViewModel.getFarmCountry()
                        )
                    }
                }
                //Register new user
                /*

                var showRegDialog by remember { mutableStateOf(false) }
                var userEmailReg by remember { mutableStateOf("") }
                var userNameReg by remember { mutableStateOf("") }
                var userPhoneReg by remember { mutableStateOf("") }
                MyInputDialog(
                    showDialog = showRegDialog,
                    title = "Register New User",
                    onConfirm = {
                        //TODO -> validate Email address, and role selected.
                        accountViewModel.registerUser(
                            email = userEmailReg,
                            name = userNameReg.trim(),
                            phone = userPhoneReg.trim()
                        )
                        if (accountViewModel.toastMessage.value != "Invalid email: $userEmailReg" && accountViewModel.toastMessage.value != "Please select a role") showRegDialog =
                            false
                    },
                    onDismiss = { showRegDialog = false }
                ) {
                    Column {
                        MyEditTextClear( // email input
                            label = stringResource(id = R.string.email_address_title),//"Email address",
                            iconLeading = Icons.Default.Email,
                            iconLeadingDescription = "Email",
                            keyboardType = KeyboardType.Email,
                            onValueChange = { text ->
                                userEmailReg = text.trim()
                            }
                        )
                        MyEditTextClear(// input name
                            label = stringResource(id = R.string.user_name_title),//"User Name",
                            iconLeading = Icons.Default.Person,
                            iconLeadingDescription = "person",
                            keyboardType = KeyboardType.Text,
                            onValueChange = { text ->
                                userNameReg = text.trim()
                            }
                        )
                        MyEditTextClear( // input phone
                            label = stringResource(id = R.string.phone_number_title),//"Phone number",
                            iconLeading = Icons.Default.Phone,
                            iconLeadingDescription = "Email",
                            keyboardType = KeyboardType.Phone,
                            onValueChange = { text ->
                                userPhoneReg = text.trim()
                            }
                        )

                        MyVerticalSpacer(height = 10)
                        var expanded by remember { mutableStateOf(false) }
                        MyBorderedColumn {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = stringResource(id = R.string.specify_access_level))//"Specify access Level")
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        Icons.Filled.ArrowDropDown,
                                        null,
                                        Modifier.rotate(if (expanded) 180f else 0f)
                                    )
                                }

                            }
                            if (expanded) {
                                AccessLevelItem(
                                    itemName = stringResource(id = R.string.level_egg_collection),//"Egg Collection",
                                    description = stringResource(id = R.string.egg_collection_description),
                                    isChecked = accountViewModel.eggCollectionAccess.value,
                                    onCheckedChanged = {
                                        accountViewModel.eggCollectionAccess.value = it
                                    })

                                AccessLevelItem(
                                    itemName = stringResource(id = R.string.level_edit_hen_count),//"Edit Hen Count",
                                    description = stringResource(id = R.string.edit_hen_count_description),
                                    isChecked = accountViewModel.editHenCountAccess.value,
                                    onCheckedChanged = {
                                        accountViewModel.editHenCountAccess.value = it
                                    })

                                AccessLevelItem(
                                    itemName = stringResource(id = R.string.level_manage_blocks_cells),//"Manage Blocks & Cells",
                                    description = stringResource(id = R.string.manage_blocks_cells_description),//"Allows the user to add, delete or rename a cell or a block.",
                                    isChecked = accountViewModel.manageBlocksCellsAccess.value,
                                    onCheckedChanged = {
                                        accountViewModel.manageBlocksCellsAccess.value = it
                                    })

                                AccessLevelItem(
                                    itemName = stringResource(id = R.string.level_manage_users),//"Manage other users",
                                    description = stringResource(id = R.string.manage_users_description),//"This will allow the user to be able to register new users to the farm, delete other user accounts and also be able to change the access level of the other users",
                                    isChecked = accountViewModel.manageUsersAccess.value,
                                    onCheckedChanged = {
                                        accountViewModel.manageUsersAccess.value = it
                                    })
                            }
                        }
                    }
                }

*/

                if (accountViewModel.accessLevel.value.manageUsers) {
                    Spacer(modifier = Modifier.height(16.dp))
                    ActionButton(
                        text = stringResource(id = R.string.manage_users),//"Manage existing users",
                        ImageVector.vectorResource(id = R.drawable.supervisor_account),
                        onClick = {
                            navigator.navigate(
                                ManageUsersScreenDestination
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButton(
                        text = "Upgrade to premium",
                        icon = Icons.Default.Star,
                        onClick = {
                            //launch Purchase flow
                            Toast.makeText(context, "upgrade clicked", Toast.LENGTH_SHORT).show()
                            accountViewModel.launchPurchaseFlow(activity)
                        }
                    )
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
    OutlinedButton(
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
