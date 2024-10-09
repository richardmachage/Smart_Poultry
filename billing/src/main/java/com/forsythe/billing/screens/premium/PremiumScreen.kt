package com.forsythe.billing.screens.premium

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PremiumScreen(
    onSubscribe : () -> Unit = {}
){
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ElevatedCard()  {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "PREMIUM",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Remove adds",
                    )
                    Text(
                        text = "Have unlimited users",
                    )
                    Text(
                        text = "Unlock scheduled automated analysis",
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "at only KES 300 per month"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        //Launch Purchase flow
                    }
                ) {
                    Text("Subscribe")
                }
            }
        }
    }
}