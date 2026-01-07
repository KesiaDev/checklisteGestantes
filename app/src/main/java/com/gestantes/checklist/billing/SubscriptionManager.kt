package com.gestantes.checklist.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Gerenciador de assinaturas usando Google Play Billing
 */
class SubscriptionManager(private val context: Context) : PurchasesUpdatedListener {

    companion object {
        // IDs dos produtos - devem corresponder aos criados no Play Console
        const val SUBSCRIPTION_MONTHLY = "premium_monthly"
        const val SUBSCRIPTION_YEARLY = "premium_yearly"
        
        @Volatile
        private var INSTANCE: SubscriptionManager? = null
        
        fun getInstance(context: Context): SubscriptionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SubscriptionManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _subscriptionProducts = MutableStateFlow<List<ProductDetails>>(emptyList())
    val subscriptionProducts: StateFlow<List<ProductDetails>> = _subscriptionProducts.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    init {
        connectToGooglePlay()
    }

    private fun connectToGooglePlay() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Conexão estabelecida
                    querySubscriptionProducts()
                    queryExistingPurchases()
                } else {
                    _isLoading.value = false
                }
            }

            override fun onBillingServiceDisconnected() {
                // Tentar reconectar
                connectToGooglePlay()
            }
        })
    }

    private fun querySubscriptionProducts() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(SUBSCRIPTION_MONTHLY)
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(SUBSCRIPTION_YEARLY)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _subscriptionProducts.value = productDetailsList
            }
            _isLoading.value = false
        }
    }

    private fun queryExistingPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val hasValidSubscription = purchases.any { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                    purchase.isAcknowledged
                }
                _isPremium.value = hasValidSubscription
                
                // Acknowledge any unacknowledged purchases
                purchases.filter { 
                    it.purchaseState == Purchase.PurchaseState.PURCHASED && !it.isAcknowledged 
                }.forEach { purchase ->
                    acknowledgePurchase(purchase)
                }
            }
        }
    }

    fun launchSubscriptionFlow(activity: Activity, productDetails: ProductDetails, offerToken: String) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    acknowledgePurchase(purchase)
                    _isPremium.value = true
                }
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            
            billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _isPremium.value = true
                }
            }
        }
    }

    fun refreshPurchases() {
        queryExistingPurchases()
    }
}

