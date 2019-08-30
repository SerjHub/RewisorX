package com.app.rewizor.remote

import android.util.Log
import com.app.rewizor.data.repositoryImpl.AccountRepositoryImpl
import com.app.rewizor.preferences.PreferencesCache
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RestClient(
    private val baseUrl: String,
    private val prefs: PreferencesCache) :
    KoinComponent,
    PreferencesCache.TokenChangeListener,
        PreferencesCache.RegionChangeListener
{
    lateinit var api: Api
    var region = "4001"
    lateinit var accessToken: String
    private val gson = GsonConverterFactory.create(GsonBuilder().create())

    private val okHttpClient = OkHttpClient.Builder().apply {
        connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
        addInterceptor(createLogger())
        addInterceptor {  chain ->
            val request = chain.request()
            val urlBuilder = request.url.newBuilder()
            urlBuilder.addQueryParameter(REGION_QUERY, region)
            urlBuilder.addQueryParameter(TOKEN_QUERY, accessToken)
            val url = urlBuilder.build()
            val builder = request.newBuilder().url(url)
            try {
                chain.proceed(builder.build())
            } catch (e: Exception) {
                throw IOException(e)
            }
        }
    }.build()

    fun setEndpoint() {
        accessToken = prefs.sessionToken ?: AccountRepositoryImpl.ANON_TOKEN
        Log.i("FindToken", "${prefs.sessionToken ?: 0}")
        with (prefs.tokenClients) {
            if (none { it == this@RestClient }) add(this@RestClient)
        }
        with (prefs.regionClients) {
            if (none { it == this@RestClient }) add(this@RestClient)
        }
        api = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(okHttpClient)
            addConverterFactory(gson)
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }.build().create(Api::class.java)
    }

    suspend fun <R> callApi(call: () -> Deferred<R>): R {
        return call().await()
    }

    private fun createLogger() =
        HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }

    override fun onTokenChanged(newToken: String) {
        accessToken = newToken
    }

    override fun onRegionChanged(newRegionId: String) {
        region = newRegionId
    }

    companion object {
        val TAG = RestClient::class.java.name
        const val API_REQUEST_LOGGER = "API_REQUEST_LOGGER"
        const val TOKEN_QUERY = "access_token"
        const val REGION_QUERY = "region"
    }
}