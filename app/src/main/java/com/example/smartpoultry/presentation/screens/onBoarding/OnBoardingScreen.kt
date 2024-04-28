package com.example.smartpoultry.presentation.screens.onBoarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.destinations.LogInScreenDestination
import com.example.smartpoultry.presentation.screens.onBoarding.components.NextBackButton
import com.example.smartpoultry.presentation.screens.onBoarding.components.OnBoardingPage
import com.example.smartpoultry.presentation.screens.onBoarding.components.PageIndicator
import com.example.smartpoultry.presentation.screens.onBoarding.components.pages
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun OnBoardingScreen(
    navigator: DestinationsNavigator
) {

    val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val pagerState = rememberPagerState(initialPage = 0) { pages.size }
        val currentPage = pagerState.currentPage
        val scope = rememberCoroutineScope()

        //Horizontal Pager
        HorizontalPager(state = pagerState) {position->
            OnBoardingPage(pages[position])
        }

        Spacer(modifier = Modifier.weight(1f))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
            ){
            PageIndicator(pageSize = pages.size, selectedPage = currentPage)
            NextBackButton(
                currentPage = currentPage,
                onNextClick = {
                              scope.launch{
                                  pagerState.animateScrollToPage(currentPage +1)
                              }
                },
                onBackClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(currentPage -1)
                    }
                },
                onGetStartedClick = {
                    onBoardingViewModel.saveAppEntry()
                    navigator.navigate(LogInScreenDestination){
                        popUpTo(LogInScreenDestination){inclusive=true}
                    }
                }
            )

        }
    }
}