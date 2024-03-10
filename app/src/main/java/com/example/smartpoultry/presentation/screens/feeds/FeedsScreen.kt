package com.example.smartpoultry.presentation.screens.feeds

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.presentation.composables.MyDatePicker
import com.example.smartpoultry.presentation.composables.MyInputDialog
import com.example.smartpoultry.presentation.composables.MyOutlineTextFiled
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun FeedsScreen() {
    val feedsViewModel = hiltViewModel<FeedsViewModel>()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }


    MyInputDialog(
        showDialog = showDialog,
        title = "Add Feeds",
        onConfirm = {
            feedsViewModel.viewModelScope.launch {
                if (feedsViewModel.onAddFeeTrackRecord() > 0) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            showDialog = false
        },
        onDismiss = {
            showDialog = false
        }
    ) {


        Column(
            modifier = Modifier.padding(6.dp)
        ) {

            MyDatePicker(
                dateDialogState = rememberMaterialDialogState(),
                label = "Date",
                positiveButtonOnClick = { localDate ->
                    feedsViewModel.feedTrackSelectedDate.value = localDate
                },
                negativeButton = {},
                allowedDateValidate = {
                    it < LocalDate.now() || it == LocalDate.now()
                }
            )

            MyOutlineTextFiled(
                label = "Number of Sacks Added",
                keyboardType = KeyboardType.Number,
                initialText = "" ,
                onValueChange = {
                    feedsViewModel.feedTrackNumOfSacks.intValue = it.toIntOrNull() ?:0
                }
            )

        }
    }

    SmartPoultryTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background

        ) {
            Column(
                modifier = Modifier.padding(6.dp)

            ) {

                Column(
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
                ) {
                    Text(text = "Sacks of Feeds in Store : 19")

                    NormButton(
                        onButtonClick = {
                            showDialog = true
                        },
                        btnName = "Add Feeds",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                MyVerticalSpacer(height = 10)

                Column(
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
                ) {
                    Text(text = "Record feed usage: ")

                    MyDatePicker(
                        modifier = Modifier.fillMaxWidth(),
                        dateDialogState = rememberMaterialDialogState(),
                        label = "Date",
                        positiveButtonOnClick = { localDate ->
                            feedsViewModel.recordSelectedDate.value = localDate
                        },
                        negativeButton = {},
                        allowedDateValidate = {
                            it < LocalDate.now() || it == LocalDate.now()
                        }
                    )

                    //var text by remember{ mutableStateOf("") }
                    MyOutlineTextFiled(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Sacks used",
                        keyboardType = KeyboardType.Number,
                        initialText = "",
                        onValueChange = {
                            feedsViewModel.recordsNumOfSacks.intValue = it.toIntOrNull() ?: 0
                        }
                    )

                    NormButton(
                        modifier = Modifier.fillMaxWidth(),
                        onButtonClick = {

                            feedsViewModel.viewModelScope.launch {
                                if(feedsViewModel.onAddFeedRecord() > 0){
                                    Toast.makeText(context,"Saved Successfully", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        btnName = "Save"
                    )
                }

                MyVerticalSpacer(height = 10)
            }
        }
    }


}
