package com.forsythe.smartpoultry.presentation.screens.accountScreen

//import com.forsythe.smartpoultry.utils.USER_ROLE_KEY
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.User
import com.forsythe.smartpoultry.domain.repository.FirebaseAuthRepository
import com.forsythe.smartpoultry.utils.EDIT_HEN_COUNT_ACCESS
import com.forsythe.smartpoultry.utils.EGG_COLLECTION_ACCESS
import com.forsythe.smartpoultry.utils.FARM_COUNTRY_KEY
import com.forsythe.smartpoultry.utils.FARM_ID_KEY
import com.forsythe.smartpoultry.utils.FARM_NAME_KEY
import com.forsythe.smartpoultry.utils.MANAGE_BLOCKS_CELLS_ACCESS
import com.forsythe.smartpoultry.utils.MANAGE_USERS_ACCESS
import com.forsythe.smartpoultry.utils.SUBSCRIPTION_ID
import com.forsythe.smartpoultry.utils.USER_EMAIL_KEY
import com.forsythe.smartpoultry.utils.USER_FIRST_NAME_KEY
import com.forsythe.smartpoultry.utils.USER_GENDER_KEY
import com.forsythe.smartpoultry.utils.USER_LAST_NAME_KEY
import com.forsythe.smartpoultry.utils.USER_PHONE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    //@ApplicationContext private val context: Context,
    private val fireBaseAuthRepo: FirebaseAuthRepository,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {
    private lateinit var billingClient: BillingClient

    var isLoading = mutableStateOf(false)
    var toastMessage = mutableStateOf("")
    var eggCollectionAccess = mutableStateOf(false)
    var editHenCountAccess = mutableStateOf(false)
    var manageUsersAccess = mutableStateOf(false)
    var manageBlocksCellsAccess = mutableStateOf(false)
    var accessLevel = mutableStateOf(getAccessLevel())
    val user = User(
        firstName = getUserFirstName(),
        lastName = getUserLastName(),
        gender = getUserGender(),
        email = getUserEmail(),
        phone = getUserPhone(),
    )
    private fun getUserFirstName() = preferencesRepo.loadData(USER_FIRST_NAME_KEY)!!
    private fun getUserLastName() = preferencesRepo.loadData(USER_LAST_NAME_KEY)!!
    private fun getUserEmail() = preferencesRepo.loadData(USER_EMAIL_KEY)!!
    private fun getUserPhone() = preferencesRepo.loadData(USER_PHONE_KEY)!!
    private fun getUserGender() = preferencesRepo.loadData(USER_GENDER_KEY)!!
    private fun getAccessLevel(): AccessLevel {
        return AccessLevel(
            manageUsers = preferencesRepo.loadData(MANAGE_USERS_ACCESS).toBoolean(),
            manageBlocksCells = preferencesRepo.loadData(MANAGE_BLOCKS_CELLS_ACCESS).toBoolean(),
            editHenCount = preferencesRepo.loadData(EDIT_HEN_COUNT_ACCESS).toBoolean(),
            collectEggs = preferencesRepo.loadData(EGG_COLLECTION_ACCESS).toBoolean()
        )

    }

    private fun getFarmId() = preferencesRepo.loadData(FARM_ID_KEY)!!
    fun getFarmName() = preferencesRepo.loadData(FARM_NAME_KEY)!!
    fun getFarmCountry() = preferencesRepo.loadData(FARM_COUNTRY_KEY)!!

    fun changeFarmName(farmName: String) {
        viewModelScope.launch {
            val result = fireBaseAuthRepo.editFarmName(farmName)
            result.onSuccess {
                toastMessage.value = "Request successful, changes will reflect on next log in"
            }
            result.onFailure {
                toastMessage.value = "failed: ${it.message.toString()}"
            }
        }
    }

    fun changeEmail(email: String) {
        viewModelScope.launch {
            val result = fireBaseAuthRepo.editEmail(email = email)
            result.onSuccess {
                toastMessage.value = "Request successful, changes will reflect on next log in"
            }
            result.onFailure {
                toastMessage.value = "failed: ${it.message.toString()}"
            }
        }
    }

    fun editFirstName(name: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = fireBaseAuthRepo.editFirstName(name)
            if (result.isSuccess) toastMessage.value =
                "Change successful, changes will reflect on next log in"
            else if (result.isFailure) toastMessage.value =
                "Failed: ${result.exceptionOrNull()?.message.toString()}"

            isLoading.value = false
        }
    }

    fun editLastName(name: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = fireBaseAuthRepo.editLastName(name)
            if (result.isSuccess) toastMessage.value =
                "Change successful, changes will reflect on next log in"
            else if (result.isFailure) toastMessage.value =
                "Failed: ${result.exceptionOrNull()?.message.toString()}"

            isLoading.value = false
        }
    }


    fun changePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = fireBaseAuthRepo.editPhone(phoneNumber)
            result.onSuccess {
                toastMessage.value = "Change successful, changes will reflect on next log in"
            }
            result.onFailure {
                toastMessage.value = "failed: ${it.message.toString()}"
            }
            isLoading.value = false
        }
    }

/*
    fun launchPurchaseFlow(activity: Activity) {
        val purchasesUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            //Implement the logic for processing purchases,
            // including acknowledgment of purchases (required for subscriptions)
        }

        //initialize billing client
        val billingClient = BillingClient
            .newBuilder(activity)
            .setListener(purchasesUpdateListener)
            .enablePendingPurchases()
            .build()

        //connect to google play
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                //connectToGooglePlay()
                //reconnect
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
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
                        if (billResult.responseCode == BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                            val productDetails = productDetailsList[0]
                            val billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(
                                    listOf(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                            .setProductDetails(productDetails)
                                            .build()
                                    )
                                )
                                .build()

                            billingClient.launchBillingFlow(activity, billingFlowParams)
                        }
                    }
                }
            }

        })
    }
*/

    fun launchPurchaseFlow(activity: Activity) {
        if (!billingClient.isReady) {
            Log.e("Billing", "Billing client is not ready")
            return
        }else{
            Log.e("Billing", "Purchase flow method initiated...")
        }

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
        Log.e("Billing", "Finished querying parameters")

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            Log.e("Billing", "queryProductDetailsAsync ${billingResult.debugMessage} code : ${billingResult.responseCode} products: ${productDetailsList.size}")
            Log.e("Billing", "The product is ${productDetailsList[0].title}")
            val productDetails = productDetailsList[0]
            Log.e("Billing", "Product ID: ${productDetails.productId}, Product Type: ${productDetails.productType}, Price: ${productDetails.oneTimePurchaseOfferDetails?.formattedPrice ?: "No price info"}")

            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )
            Log.e("Billing", "ProductDetailsParams list has ${productDetailsParamsList.size} items")

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

            // Launch the billing flow
            val launchResult = billingClient.launchBillingFlow(activity, billingFlowParams)
            if (launchResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("Billing", "Purchase flow launched successfully")
            } else {
                Log.e("Billing", "Error launching purchase flow: ${launchResult.debugMessage} code: ${launchResult.responseCode}")
            }
        }
    }

    fun initBillingClient(context: Context) {
        val purchasesUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
            // Handle the result of a purchase
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    // Process each purchase
                    handlePurchase(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle user canceling the purchase flow
                Log.d("Billing", "User canceled the purchase flow")
            } else {
                // Handle other error cases
                Log.e("Billing", "Error: ${billingResult.debugMessage}")
            }
        }

        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdateListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // Try to reconnect or notify the user of the failure
                Log.w("Billing", "Billing service disconnected")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // BillingClient is ready
                    Log.d("Billing", "Billing setup finished successfully")
                } else {
                    // Handle any errors in setup
                    Log.e("Billing", "Error in billing setup: ${billingResult.debugMessage}")
                }
            }
        })
    }

    private fun handlePurchase(purchase: Purchase) {
        // Handle the purchase (e.g., verify with your server)
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Acknowledge the purchase if it's not yet acknowledged
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d("Billing", "Purchase acknowledged successfully")
                    } else {
                        Log.e("Billing", "Failed to acknowledge purchase: ${billingResult.debugMessage}")
                    }
                }
            }
        }
    }
}