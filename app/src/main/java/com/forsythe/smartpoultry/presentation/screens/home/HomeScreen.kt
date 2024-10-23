package com.forsythe.smartpoultry.presentation.screens.home

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.widget.Toast
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.data.dataModels.DailyEggCollection
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.presentation.NavGraphs
import com.forsythe.smartpoultry.presentation.composables.ads.BannerAd
import com.forsythe.smartpoultry.presentation.composables.ads.rewardedAdSmartPoultry
import com.forsythe.smartpoultry.presentation.composables.buttons.MyOutlineButton
import com.forsythe.smartpoultry.presentation.composables.cards.MyCard
import com.forsythe.smartpoultry.presentation.composables.cards.MyCardInventory
import com.forsythe.smartpoultry.presentation.composables.charts.RecentEggsLineChart
import com.forsythe.smartpoultry.presentation.composables.dialogs.ConfirmDialog
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.forsythe.smartpoultry.presentation.composables.spacers.MyHorizontalSpacer
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.text.NormText
import com.forsythe.smartpoultry.presentation.composables.text.TitleText
import com.forsythe.smartpoultry.presentation.destinations.LogInScreenDestination
import com.forsythe.smartpoultry.utils.BANNER_AD_ID
import com.forsythe.smartpoultry.utils.EXPORT_TO_PDF_REWARDED_AD_ID
import com.forsythe.smartpoultry.utils.PAST_DAYS_KEY
import com.forsythe.smartpoultry.utils.isNetworkAvailable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import java.text.SimpleDateFormat


@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.O)
@Destination
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    //modifier: Modifier
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    //viewmodel initialization
    val homeViewModel: HomeViewModel = hiltViewModel()
    val totalBlocks = homeViewModel.totalBlocks.collectAsState()
    val totalCells =
        homeViewModel.totalCells.collectAsState() // val userRole by homeViewModel.userRole.collectAsState()
    val userName = homeViewModel.getName()!!
    val emailAddress =
        homeViewModel.getEmail()!!//by homeViewModel.dataStore.readData(USER_EMAIL_KEY).collectAsState(initial = "")

    val listOfAlerts by remember { homeViewModel.getFlaggedCells() }.collectAsState(initial = emptyList())


    val pastDaysState =
        homeViewModel.preferencesRepo.loadData(PAST_DAYS_KEY)//remember { homeViewModel.dataStore.readData(PAST_DAYS_KEY) }.collectAsState(initial = "0")
    val pastDays = pastDaysState?.toIntOrNull() ?: 0

    val dailyEggsForPastDays: State<List<DailyEggCollection>> =
        produceState(initialValue = emptyList(), key1 = pastDays) {
            if (pastDays > 0) {
                homeViewModel.getOverallCollectionsForPastDays(pastDays).collect {
                    value = it
                }
            }
        }

    LaunchedEffect(homeViewModel.toastMessage) {
        if (homeViewModel.toastMessage.isNotBlank()) {
            Toast.makeText(context, homeViewModel.toastMessage, Toast.LENGTH_SHORT).show()
            homeViewModel.toastMessage = ""
        }
    }

    LaunchedEffect(homeViewModel.navigateToLogin) {
        if (homeViewModel.navigateToLogin.isNotBlank()) {
            homeViewModel.navigateToLogin = ""
            navigator.navigate(LogInScreenDestination) {
                popUpTo(NavGraphs.root) { inclusive = true }
            }
        }
    }
//leave app dialog
    var showLeaveDialog by remember { mutableStateOf(false) }
    MyInputDialog(
        showDialog = showLeaveDialog,
        title = "Leave Smart Poultry",
        onConfirm = {
            (context as Activity).finish()
        },
        onDismiss = {
            showLeaveDialog = false
        }
    ) {
        Text(text = "Are you sure you want to leave the App?")
    }


    DisposableEffect(key1 = Unit) {
        // Cast the context to an Activity, if possible
        val activity = context as? ComponentActivity

        // Register a back callback for Android 14 and above
        val backCallback = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Use OnBackInvokedDispatcher for Android 14 and above
            val callback = OnBackInvokedCallback {
                // Show the leave dialog when the back button is pressed
                showLeaveDialog = true
            }
            activity?.onBackInvokedDispatcher?.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                callback
            )
            callback
        } else {
            // Fallback for older versions of Android
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Show the leave dialog when the back button is pressed
                    showLeaveDialog = true
                }
            }
            activity?.onBackPressedDispatcher?.addCallback(activity, callback)
            callback
        }

        onDispose {
            // Cleanup: Unregister the back callback when the composable leaves the composition
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                activity?.onBackInvokedDispatcher?.unregisterOnBackInvokedCallback(backCallback as OnBackInvokedCallback)
            } else {
                (backCallback as? OnBackPressedCallback)?.remove()
            }
        }
    }


    MyCircularProgressBar(
        isLoading = homeViewModel.isLoading,
        displayText = homeViewModel.isLoadingText
    )

    Box (
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ){
        Column( //Full Screen column
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            var showPasswordResetDialog by remember {
                mutableStateOf(false)
            }

            //change password dialog here
            MyInputDialog(
                showDialog = showPasswordResetDialog,
                title = "Reset Password",
                onConfirm = { /*TODO*/
                    homeViewModel.onPasswordReset(emailAddress)
                    showPasswordResetDialog = false
                },
                onDismiss = {
                    /*TODO*/
                    showPasswordResetDialog = false
                }
            ) {
                //Dialog body content
                Column {
                    Text(text = "A password reset link will be sent to your email address: $emailAddress")
                    MyVerticalSpacer(height = 10)
                    Text(
                        text = "After clicking okay, follow these steps \n" +
                                "1.You will be logged out automatically \n" +
                                "2.Go to your email inbox \n" +
                                "3.Click on link sent to reset your password \n" +
                                "4.Now open Smart Poultry and log in using your new password"
                    )
                }
            }

            //show ad dialog
            var showAdDialog by remember { mutableStateOf(false) }
            ConfirmDialog(
                showDialog = showAdDialog,
                title = stringResource(R.string.show_add),
                message = stringResource(R.string.pdf_ad_message),
                onDismiss = {
                    showAdDialog = false
                },
                onConfirm = {
                    //todo go on to show add and export data as reward

                    rewardedAdSmartPoultry(
                        context = context,
                        adUnitId = EXPORT_TO_PDF_REWARDED_AD_ID,
                        coroutineScope = homeViewModel.viewModelScope,
                        onRewardEarned = {
                            val reportType = "Inventory Status"
                            homeViewModel.onCreateReport(
                                name = "$reportType ${SimpleDateFormat("dd/MMM/yyyy").format(System.currentTimeMillis())}",
                                content =
                                "\nTotal Blocks : ${totalBlocks.value.size}" +
                                        "\nTotal Cells: ${totalCells.value.size}" +
                                        "\nTotal Chicken: ${totalCells.value.sumOf { cell: Cells -> cell.henCount }}",
                                reportType = reportType
                            )
                            Toast.makeText(
                                context,
                                R.string.export_inventory_success,
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onDismiss = {
                            showAdDialog = false
                        },
                        onFailedToLoadAd = {
                            showAdDialog = false
                            Toast.makeText(context,"Failed to load ad" ,Toast.LENGTH_SHORT).show()
                        }
                    )

                }
            )
            if (homeViewModel.passwordReset.value == "false") {
                MyCard(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                ) {
                    TextButton(onClick = {
                        //show password reset dialog
                        showPasswordResetDialog = true
                    }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = "info")
                            NormText(text = "To reset your password, click here")
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    //Inventory block
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp),

                    ) {
                    TitleText(
                        modifier = Modifier.padding(5.dp),
                        text = "${homeViewModel.farmName.value} Inventory",//stringResource(id = R.string.inventory_home_screen)
                    )


                    Row( //inventory cards
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        MyCardInventory(
                            modifier = Modifier.weight(1f),
                            item = "Chicken",
                            number = totalCells.value.sumOf { cell: Cells -> cell.henCount }
                        )
                        MyHorizontalSpacer(width = 5)
                        MyCardInventory(
                            modifier = Modifier.weight(1f),
                            item = "Blocks",
                            number = totalBlocks.value.size
                        )
                        MyHorizontalSpacer(width = 5)

                        MyCardInventory(
                            modifier = Modifier.weight(1f),
                            item = "Cells",
                            number = totalCells.value.size
                        )

                    }

                    MyOutlineButton( //export to pdf
                        modifier = Modifier.fillMaxWidth(),
                        onButtonClick = {
                            showAdDialog = true
                        },
                        btnName = stringResource(id = R.string.export_inventory_summary_as_pdf)
                    )
                }
            }

            MyVerticalSpacer(height = 5)
            //Greeting card
            if (userName.isNotBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "👋🏼 " + stringResource(id = R.string.greeting_home) + ", $userName. " + stringResource(
                        id = homeViewModel.getGreetingBasedOnTime()
                    ),
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.W300
                )

            }
            MyVerticalSpacer(height = 5)

            AnimatedVisibility(visible = dailyEggsForPastDays.value.isNotEmpty()) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        //Recent production trends block
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(6.dp),
                    ) {
                        TitleText(
                            modifier = Modifier.padding(start = 5.dp),
                            text = stringResource(id = R.string.recent_production_trends_home)
                        )
                        MyVerticalSpacer(height = 10)
                        if (dailyEggsForPastDays.value.isNotEmpty()) RecentEggsLineChart(
                            dailyEggCollections = dailyEggsForPastDays.value.reversed()
                        )
                    }
                }
            }

            if (
                //TODO implement condition to check subscription status
                context.isNetworkAvailable()
            ) {
                MyVerticalSpacer(10)
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BannerAd(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        adId = BANNER_AD_ID
                    )
                }
            }
        }
    }
    // }
}