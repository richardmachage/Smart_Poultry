package com.forsythe.smartpoultry.presentation.composables.cards

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun MyCard(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
){
    Card(
        modifier = modifier
            .animateContentSize()
            //.padding(8.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
            )
            .width(
                (LocalConfiguration.current.screenWidthDp / 4).dp
            ),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContentColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(20),

        ) {
        content()
    }
}
