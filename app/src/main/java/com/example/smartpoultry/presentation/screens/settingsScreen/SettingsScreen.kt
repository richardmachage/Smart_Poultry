package com.example.smartpoultry.presentation.screens.settingsScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.presentation.composables.MyBorderedColumn
import com.example.smartpoultry.presentation.composables.MyBorderedRow
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen(
   // navigator:DestinationsNavigator
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = {
                    IconButton(onClick = {
                       // navigator.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                Modifier
                    .padding(6.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                MyBorderedRow(

                ) {
                    var isDarkMode by remember{ mutableStateOf(false) }
                    Text(text = if (isDarkMode) "Switch to light mode" else "Switch to dark mode")
                    Switch(checked = !isDarkMode, onCheckedChange =  {
                        isDarkMode = !isDarkMode
                    })
                }

                MyVerticalSpacer(height = 10)

                Column ( //Past Days
                    Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(
                                (0.03 * LocalConfiguration.current.screenWidthDp).dp
                            )
                        )
                        .fillMaxWidth()
                        .padding(6.dp),
                ){
                    Text(text = "Past Days Summary in Home Screen: ")
                    MyOutlineTextFiled(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Days",
                        keyboardType = KeyboardType.Number,
                        onValueChange = {

                        }
                    )
                }
                MyVerticalSpacer(height = 10)
                Column(
                    Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(
                                (0.03 * LocalConfiguration.current.screenWidthDp).dp
                            )
                        )
                        .fillMaxWidth()
                        .padding(6.dp)
                ) {
                    Text(text = "Number of Day for trend analysis (Consucutive days of low production to be considered before flagging a cell?)")
                    MyOutlineTextFiled(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Days",
                        keyboardType = KeyboardType.Number,
                        onValueChange = {}
                    )
                }

                MyVerticalSpacer(height = 10)

                MyBorderedColumn {
                    Text(text = "Threshold Ratio for trend analysis (What should be the minimum henCount to EggCount ration in determining poor egg production?)")
                    MyOutlineTextFiled(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Threshold Ratio",
                        keyboardType = KeyboardType.Decimal,
                        onValueChange = {}
                    )
                }
                MyVerticalSpacer(height = 10)

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /*TODO*/ }) {
                    Text(text = "Log Out")
                }
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevSettings() {
    SettingsScreen()
}