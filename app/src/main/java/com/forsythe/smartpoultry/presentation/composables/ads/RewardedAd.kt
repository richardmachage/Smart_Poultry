package com.forsythe.smartpoultry.presentation.composables.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

fun rewardedAdSmartPoultry(
    context: Context,
    adUnitId: String,
    coroutineScope: CoroutineScope,
    onRewardEarned: () -> Unit,
    onFailedToLoadAd: () -> Unit,
    onDismiss: () -> Unit
) {
    suspend fun loadRewardedAdd(context: Context): RewardedAd? {
        return suspendCancellableCoroutine { continuation ->
            RewardedAd.load(context, adUnitId, AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        continuation.resume(null)
                        onFailedToLoadAd()
                    }

                    override fun onAdLoaded(loadedAd: RewardedAd) {
                        super.onAdLoaded(loadedAd)
                        continuation.resume(loadedAd)
                    }
                }
            )
        }
    }

    suspend fun showRewardedAdd(context: Context, onDismissed: () -> Unit) {
        loadRewardedAdd(context)?.let {
            it.show(
                context as Activity
            ) {
                onRewardEarned()
                onDismissed()
            }
        }

    }

    coroutineScope.launch {
        showRewardedAdd(
            context = context,
            onDismissed = onDismiss
        )
    }
}