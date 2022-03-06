package io.stacrypt.stadroid.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public object StemeraldClientFactory {

    public fun createApiKeyClient(apiKey: ApiKey, baseUrl: String = STEMERALD_V2_API_URL): StemeraldV2ApiClient {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val newRequest = chain.request()
                            .newBuilder()
                            .addHeader("X-Api-Key", apiKey.apikey)
                            .addHeader("X-Api-Secret", apiKey.apiSecret)
                            .build()
                        val response = chain.proceed(newRequest)
                        return@addInterceptor response

                    }
                    .addInterceptor(HttpLoggingInterceptor())
                    .build()

            )
            .build()
            .create(StemeraldV2ApiClient::class.java)

    }

    public fun createSessionClient(baseUrl: String = STEMERALD_V2_API_URL): StemeraldV2ApiClient {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
//                  .cookieJar(cookieJar)
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

                        if (response.code == 401) {
                            // TODO: Force logout
                            sessionManager.logout()
                        }
                        return@addInterceptor response

                    }
                    .addInterceptor(HttpLoggingInterceptor())
                    .build()
            )
            .build()
            .create(StemeraldV2ApiClient::class.java)

    }

}