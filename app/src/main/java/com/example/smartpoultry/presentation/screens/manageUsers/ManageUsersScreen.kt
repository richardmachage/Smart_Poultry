package com.example.smartpoultry.presentation.screens.manageUsers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ManageUsersScreen(){
    SmartPoultryTheme {
        /*
        Show list of the users
         */
        LazyColumn {
            items(count = 7){
                UserItem()
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
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrevManageUsers(){
    ManageUsersScreen()
}