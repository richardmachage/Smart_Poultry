package com.forsythe.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams


class PlayBilling(
    private val context: Context
) {
    private val purchasesUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
        // To be implemented in a later section.
    }

    //initialize billing client
    private val billingClient = BillingClient
        .newBuilder(context)
        .setListener(purchasesUpdateListener)
        .build()

    //connect to google play
    fun connectToGooglePlay() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                connectToGooglePlay()
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    queryAvailableSubscriptions()
                }
            }

        })
    }

    private fun queryAvailableSubscriptions(){
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(SUBSCRIPTION_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billResult, productDetailsList ->
            if (billResult.responseCode == BillingResponseCode.OK && productDetailsList.isNotEmpty()){
                val productDetails = productDetailsList[0]
                launchSubscriptionPurchaseFlow(productDetails)
            }
        }
    }

    private fun launchSubscriptionPurchaseFlow(productDetails: ProductDetails, ) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            )
            .build()

        billingClient.launchBillingFlow(this.context as Activity , billingFlowParams)
    }
}