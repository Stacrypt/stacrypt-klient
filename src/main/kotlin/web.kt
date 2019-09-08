package io.stacrypt.stadroid.data

//import android.util.Base64
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.util.*
//import io.fabric.sdk.android.services.settings.IconRequest.build
import okhttp3.*

//import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
//import com.franmontiel.persistentcookiejar.cache.SetCookieCache
//import com.franmontiel.persistentcookiejar.PersistentCookieJar
//import io.stacrypt.stadroid.application


var STEMERALD_V2_API_URL = "http://stemerald.stacrypt.io/apiv2/"


@Suppress("DeferredIsResult")
interface StemeraldV2ApiClient {

    /**
     * Assets
     */
    @HTTP(method = "LIST", path = "assets", hasBody = true)
    fun assetList(): Deferred<ArrayList<Asset>>

    @HTTP(method = "GET", path = "currencies", hasBody = false)
    fun currencyList(): Deferred<ArrayList<Currency>>

    /**
     * Balances
     */
    @HTTP(method = "OVERVIEW", path = "balances", hasBody = true)
    fun balanceList(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""
    ): Deferred<ArrayList<BalanceOverview>>

    @HTTP(method = "OVERVIEW", path = "balances", hasBody = true)
    fun balanceOverview(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""
    ): Deferred<ArrayList<BalanceOverview>>

    @HTTP(method = "HISTORY", path = "balances", hasBody = true)
    fun balanceHistory(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Query("asset") assetName: String,
        @Query("page") page: Int = 0
    ): Deferred<ArrayList<BalanceHistory>>

    /**
     * Markets
     */
    @HTTP(method = "LIST", path = "markets", hasBody = true)
    fun marketList(): Deferred<ArrayList<Market>>

    @HTTP(method = "STATUS", path = "markets/{market}", hasBody = true)
    fun marketStatus(@Path("market") market: String, @Query("period") period: Long = 86400): Deferred<MarketStatus>

    @HTTP(method = "SUMMARY", path = "markets/{market}", hasBody = true)
    fun marketSummary(@Path("market") market: String): Deferred<ArrayList<MarketSummary>>

    @HTTP(method = "LAST", path = "markets/{market}", hasBody = true)
    fun marketLast(@Path("market") market: String): Deferred<MarketLast>

    @HTTP(method = "KLINE", path = "markets/{market}", hasBody = true)
    fun kline(
        @Path("market") market: String,
        @Query("start") start: Int,
        @Query("end") end: Int,
        @Query("interval") interval: Int = 86400
    ): Deferred<ArrayList<Kline>>

    @HTTP(method = "BOOK", path = "markets/{market}", hasBody = true)
    fun book(@Path("market") market: String, @Query("side") side: String): Deferred<List<Book>>

    @HTTP(method = "DEPTH", path = "markets/{market}", hasBody = true)
    fun depth(
        @Path("market") market: String,
        @Query("interval") interval: String?,
        @Query("limit") limit: Int
    ): Deferred<Depth>

    @HTTP(method = "PEEK", path = "markets/{market}/marketdeals", hasBody = true)
    fun deal(
        @Path("market") market: String,
        @Query("limit") take: Int = 20,
        @Query("lastId") lastId: Int = 0
    ): Deferred<ArrayList<Deal>>

    /**
     * Orders
     */
    @HTTP(method = "GET", path = "orders", hasBody = false)
    fun getOrders(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Query("marketName") marketName: String,
        @Query("status") status: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Deferred<ArrayList<Order>>

    @HTTP(method = "CANCEL", path = "orders/{order}", hasBody = true)
    fun cancelOrder(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Path("order") orderId: Int,
        @Query("marketName") marketName: String
    ): Deferred<Unit>

    @FormUrlEncoded
    @HTTP(method = "CREATE", path = "orders", hasBody = true)
    fun putMarketOrder(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("marketName") marketName: String,
        @Field("amount") amount: String,
        @Field("side") side: String,
        @Field("type") type: String = "market"
    ): Deferred<Order>

    @FormUrlEncoded
    @HTTP(method = "CREATE", path = "orders", hasBody = true)
    fun putLimitOrder(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("marketName") marketName: String,
        @Field("amount") amount: String,
        @Field("price") price: String,
        @Field("side") side: String,
        @Field("type") type: String = "limit"
    ): Deferred<Order>

    /**
     * Wallet
     */
    @HTTP(method = "PEEK", path = "markets/{market}/mydeals", hasBody = true)
    fun mine(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Path("market") market: String,
        @Query("offset") skip: Int = 0,
        @Query("limit") take: Int = 20
    ): Deferred<ArrayList<Mine>>

    @HTTP(method = "SHOW", path = "deposits", hasBody = true)
    fun showDepositInfo(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Query("cryptocurrencySymbol") assetName: String
    ): Deferred<DepositInfo>

    @HTTP(method = "RENEW", path = "deposits", hasBody = true)
    fun renewDepositInfo(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Query("cryptocurrencySymbol") assetName: String
    ): Deferred<DepositInfo>

    @HTTP(method = "GET", path = "transactions/payment-gateways", hasBody = false)
    fun getPaymentGateways(): Deferred<List<PaymentGateway>>

    /**
     * Membership
     */
    @HTTP(method = "GET", path = "clients/me", hasBody = false)
    fun me(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""
    ): Deferred<User>

    @FormUrlEncoded
    @HTTP(method = "POST", path = "sessions", hasBody = true)
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Deferred<TokenResponse>

    @FormUrlEncoded
    @HTTP(method = "REGISTER", path = "clients", hasBody = true)
    fun registerNewClient(
        @Field("email") email: String,
        @Field("password") password: String
    ): Deferred<User>


    /**
     * Email verification
     */
    @HTTP(method = "SCHEDULE", path = "clients/email-verifications", hasBody = true)
    fun schedulEmailVerification(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""
    ): Deferred<Unit>

    @FormUrlEncoded
    @HTTP(method = "VERIFY", path = "clients/email-verifications", hasBody = true)
    fun verifyEmailVerification(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("token") token: String
    ): Deferred<User>

    /**
     * Phone verification
     */
    @FormUrlEncoded
    @HTTP(method = "SCHEDULE", path = "clients/mobile-phone-verifications", hasBody = true)
    fun schedulMobilePhoneVerification(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("phone") phone: String
    ): Deferred<Unit>

    @FormUrlEncoded
    @HTTP(method = "SCHEDULE", path = "clients/fixed-phone-verifications", hasBody = true)
    fun schedulFixedPhoneVerification(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("phone") phone: String
    ): Deferred<Unit>

    @FormUrlEncoded
    @HTTP(method = "VERIFY", path = "clients/mobile-phone-verifications", hasBody = true)
    fun verifyMobilePhoneVerification(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("phone") phone: String,
        @Field("code") code: String
    ): Deferred<User>

    @FormUrlEncoded
    @HTTP(method = "VERIFY", path = "clients/fixed-phone-verifications", hasBody = true)
    fun verifyFixedPhoneVerification(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("phone") phone: String,
        @Field("code") code: String
    ): Deferred<User>

    /**
     * Bank Accounts
     */
    @HTTP(method = "GET", path = "banking/accounts", hasBody = false)
    fun getBankAccounts(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Query("skip") skip: Int = 0,
        @Query("take") take: Int = 20
    ): Deferred<List<BankAccount>>


    @FormUrlEncoded
    @HTTP(method = "ADD", path = "banking/accounts", hasBody = true)
    fun addBankAccount(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("iban") iban: String,
        @Field("fiatSymbol") fiatSymbol: String,
        @Field("owner") owner: String
    ): Deferred<BankAccount>

    /**
     * Bank Cards
     */
    @HTTP(method = "GET", path = "banking/cards", hasBody = false)
    fun getBankCards(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Query("skip") skip: Int = 0,
        @Query("take") take: Int = 20
    ): Deferred<List<BankCard>>

    @FormUrlEncoded
    @HTTP(method = "ADD", path = "banking/cards", hasBody = true)
    fun addBankCard(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("pan") pan: String,
        @Field("fiatSymbol") fiatSymbol: String,
        @Field("holder") holder: String
    ): Deferred<BankCard>

    /**
     * Evidences
     */
    @HTTP(method = "GET", path = "clients/me/evidences", hasBody = false)
    fun getMyEvidences(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""
    ): Deferred<Evidence>

    @Multipart
    @HTTP(method = "SUBMIT", path = "clients/evidences", hasBody = true)
    fun submitMyEvidences(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Part("firstName") firstName: String,
        @Part("lastName") lastName: String,
        @Part("gender") gender: Gender,
        @Part("birthday") birthday: Date,
        @Part("cityId") cityId: Int,
        @Part("nationalCode") nationalCode: String,
        @Part("address") address: String,
        @Part("idCard") idCard: File,
        @Part("idCardSecondary") idCardSecondary: File,
        @Part("holder") holder: String
    ): Deferred<Evidence>

    /**
     * Password
     */
    @FormUrlEncoded
    @HTTP(method = "CHANGE", path = "clients/passwords", hasBody = true)
    fun changePassword(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Field("currentPassword") currentPassword: String,
        @Field("newPassword") newPassword: String
    ): Deferred<User>

    @FormUrlEncoded
    @HTTP(method = "RESET", path = "clients/passwords", hasBody = true)
    fun doResetPasswordPassword(
        @Field("token") token: String,
        @Field("password") password: String
    ): Deferred<Unit>

    /**
     * Second Factor
     */
    @FormUrlEncoded
    @HTTP(method = "ENABLE", path = "clients/secondfactors", hasBody = true)
    fun enableSecondFactor(@Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""): Deferred<Unit>

    @FormUrlEncoded
    @HTTP(method = "PROVISION", path = "clients/secondfactors", hasBody = true)
    fun provisionSecondFactor(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""
    ): Deferred<String>

    @FormUrlEncoded
    @HTTP(method = "DISABLE", path = "clients/secondfactors", hasBody = true)
    fun disableSecondFactor(@Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""): Deferred<Unit>

    /**
     * Security logs
     */
    @HTTP(method = "GET", path = "logs", hasBody = false)
    fun getSecurityLogs(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""
    ): Deferred<List<SecurityLog>>

    /**
     * Sessions
     */
    @HTTP(method = "GET", path = "sessions", hasBody = false)
    fun getSessions(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: ""
    ): Deferred<List<Session>>

    @HTTP(method = "GET", path = "sessions/{sessionId}", hasBody = true)
    fun terminateSessions(
        @Header("Authorization") jwtToken: String = sessionManager.jwtToken ?: "",
        @Path("sessionId") sessionId: String
    ): Deferred<Unit>


}


//val cookieJar by lazy { PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(application)) }

val StemeraldOkHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
//        .cookieJar(cookieJar)
        .addInterceptor { chain ->

            val newRequest = chain.request()
                .newBuilder()
                .addHeader("X-Firebase-Token", sessionManager.fbToken ?: "")
                .build()

            val response = chain.proceed(newRequest)

            val newToken = response.header("X-New-JWT-Token")
            if (newToken != null) {
                sessionManager.login(newToken)
            }

            if (response.code() == 401) {
                // TODO: Force logout
                sessionManager.logout()
            }
            return@addInterceptor response

        }
        .addInterceptor(HttpLoggingInterceptor())
        .build()
}


val StemeraldApiClient: StemeraldV2ApiClient by lazy {
    Retrofit.Builder()
        .baseUrl(STEMERALD_V2_API_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(StemeraldOkHttpClient)
        .build()
        .create(StemeraldV2ApiClient::class.java)
}

