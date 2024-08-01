package com.example.smartpoultry.presentation.composables.searchBars

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.presentation.composables.others.MyDatePicker
import com.example.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MySearchByDate(
    onSearch : (LocalDate) -> Unit = {},
){
    var selectedDate by remember{ mutableStateOf(LocalDate.now()) }
    Row (
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(
                    (0.03 * LocalConfiguration.current.screenWidthDp).dp
                )
            )
            .fillMaxWidth()
            .padding(6.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ){
        MyDatePicker(
            dateDialogState = rememberMaterialDialogState(),
            label = "Date",
            positiveButtonOnClick = {
                                    selectedDate = it
            },
            negativeButton = {}
        )

        IconButton(
            onClick = { onSearch(selectedDate) }
        )
        {
            Icon(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                ,
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Prev(){
    SmartPoultryTheme {
        Surface {
            Column (modifier = Modifier.padding(6.dp)){
                MySearchByDate()
            }
        }
    }

}