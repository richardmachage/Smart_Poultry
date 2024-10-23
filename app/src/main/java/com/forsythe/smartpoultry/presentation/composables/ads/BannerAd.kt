package com.forsythe.smartpoultry.presentation.composables.ads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(modifier: Modifier = Modifier, adId : String){
    Column (modifier = modifier){
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {context->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = adId
                    //Request Ad
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}