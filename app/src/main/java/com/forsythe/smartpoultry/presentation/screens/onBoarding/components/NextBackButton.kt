package com.forsythe.smartpoultry.presentation.screens.onBoarding.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NextBackButton(
    currentPage: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentPage != 0) {
            Button(onClick = { onBackClick() }) {
                Text(text = "Previous")
            }
            /* TextButton(onClick = { onBackClick() }) {
                 Text(text = "Previous", color = MaterialTheme.colorScheme.primary)
             }*/
        }

        Spacer(modifier = Modifier.size(8.dp))

        Button(onClick = {
            if (currentPage == 3) onGetStartedClick() else onNextClick()
        }) {
            Text(
                text =
                if (currentPage == 3) "Get Started" else "Next"
            )
        }

    }
}