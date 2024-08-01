package com.example.smartpoultry.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.presentation.composables.dialogs.MyInputDialog

@Composable
fun AccessLevelItem(
    itemName : String,
    description : String,
    isChecked : Boolean,
    onCheckedChanged : (Boolean) -> Unit,
){
    var showDialog by remember{ mutableStateOf(false) }

    Card (
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),

    ){
        Row (modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth(0.9f)
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // var isChecked by remember{ mutableStateOf(false) }
                MyInputDialog(
                    showDialog = showDialog,
                    title = itemName,
                    onConfirm = {
                        onCheckedChanged(true)
                        showDialog = false
                                },
                    onDismiss = {
                        onCheckedChanged(false)
                        showDialog = false }) {
                    Text(text = description)
                }

                Text(text = itemName, fontWeight = FontWeight.Bold)
                Checkbox(checked = isChecked, onCheckedChange = {
                    onCheckedChanged(it)
                })
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "more info"
                )
            }
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccessLevelPrev(){
    AccessLevelItem(itemName = "collect eggs", description ="User will be able to collect eggs and enter the collected data " , false,{})
}
