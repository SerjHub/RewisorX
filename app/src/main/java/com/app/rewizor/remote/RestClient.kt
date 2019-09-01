package com.app.rewizor.remote

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
            urlBuilder.addQueryParameter(REGION_QUERY, region ?: "4001")
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
        setSettings()
        api = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(okHttpClient)
            addConverterFactory(gson)
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }.build().create(Api::class.java)
    }

    private fun setSettings() {
        with(prefs) {
            accessToken = sessionToken ?: AccountRepositoryImpl.ANON_TOKEN
            region = if (savedRegionId == 0) region else "$savedRegionId"

            // listen for changing regions and tokens
            with (tokenClients) {
                if (none { it == this@RestClient }) add(this@RestClient)
            }
            with (regionClients) {
                if (none { it == this@RestClient }) add(this@RestClient)
            }
        }

    }

    suspend fun <R> callApi(call: () -> Deferred<R>): R {
        return call().await()
    }

    private fun createLogger() =
        HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }

    override fun onTokenChanged(newToken: String) {
        accessToken = newToken
    }

    override fun onRegionChanged(newRegionId: Int) {
        region = if(newRegionId == 0) region else "$newRegionId"
    }

    companion object {
        val TAG = RestClient::class.java.name
        const val API_REQUEST_LOGGER = "API_REQUEST_LOGGER"
        const val TOKEN_QUERY = "access_token"
        const val REGION_QUERY = "region"
    }
}