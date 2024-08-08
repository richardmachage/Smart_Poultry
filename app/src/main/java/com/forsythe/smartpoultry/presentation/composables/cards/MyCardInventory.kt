package com.forsythe.smartpoultry.presentation.composables.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer

@Composable
fun MyCardInventory(
    modifier : Modifier = Modifier,
    item: String,
    number: Int,
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier,
        //.width((LocalConfiguration.current.screenWidthDp / 4).dp),
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)

    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            MyVerticalSpacer(height = 5)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = number.toString(),
                textAlign = TextAlign.Center,
            )

        }
    }
}
