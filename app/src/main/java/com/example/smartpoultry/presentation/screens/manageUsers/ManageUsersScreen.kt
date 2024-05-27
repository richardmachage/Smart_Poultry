package com.example.smartpoultry.presentation.screens.manageUsers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.presentation.composables.MyHorizontalSpacer
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ManageUsersScreen(
    navigator:DestinationsNavigator
){
    val manageUsersViewModel : ManageUsersViewModel = hiltViewModel()
    SmartPoultryTheme {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Manage Users") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator.navigateUp()
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription ="back" )
                        }
                    }
                )}
        ) {
            LazyColumn(
                modifier = Modifier.padding(it)
            ) {

                items(sampleUserList()){
                   // UserItem()
                    UserListItem(user = it)
                    Text(text = "users: ${manageUsersViewModel.listOfUsers.size}")

                }
            }
        }

    }
}

@Composable
fun UserItem(
    onOptionsClick : (userId : String) -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
           // HorizontalDivider()
            Text(text = "User Name")
            Text(text = "Current role")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    // .padding(horizontal = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        color = Color.Green
                    )
            )
            IconButton(onClick = { onOptionsClick("test Id") }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription ="more" )
            }
        }
    }
    HorizontalDivider()
}

@Composable
fun UserListItem(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // .padding(start = 6.dp, end = 6.dp)
            .clickable {

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
                text = user.name.first().uppercaseChar().toString(),
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
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                fontSize = 20.sp
            )
            Text(text = user.phone, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp)
            Text(text = user.email, style = MaterialTheme.typography.bodySmall, fontSize = 14.sp)
            Text(
                text = user.role,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                ),
                fontSize = 14.sp
            )
        }
    }
    MyVerticalSpacer(height = 5)
}

fun sampleUserList(): List<User> {
    return listOf(
        User(name = "John Doe", phone = "+1234567890", email = "johndoe@example.com", role = "Admin"),
        User(name = "Jane Smith", phone = "+0987654321", email = "janesmith@example.com", role = "User"),
        User(name = "Alice Johnson", phone = "+1122334455", email = "alicejohnson@example.com", role = "Moderator")
    )
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrevManageUsers(){
    //ManageUsersScreen()
}