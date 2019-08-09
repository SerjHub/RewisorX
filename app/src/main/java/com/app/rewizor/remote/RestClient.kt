package com.app.rewizor.remote

import android.util.Log
import com.app.rewizor.preferences.PreferencesCache
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import devcsrj.okhttp3.logging.HttpLoggingInterceptor
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RestClient(prefs: PreferencesCache,private val baseUrl: String) : KoinComponent {
    lateinit var api: Api
    private val accessToken: String = prefs.sessionToken ?: ""

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor())
        addInterceptor {  chain ->
            val request = chain.request()
            val urlBuilder = request.url.newBuilder()
            urlBuilder.addQueryParameter(TOKEN_QUERY, token)
            val url = urlBuilder.build()
            val builder = request.newBuilder().url(url)
            try {
                chain.proceed(builder.build())
            } catch (e: Exception) {
                Log.e(API_REQUEST_LOGGER, e.message)
                throw IOException(e)
            }
        }
    }.build()

    fun setEndpoint() {
        api = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }.build().create(Api::class.java)
    }

    suspend fun <R> callApi(call: () -> Deferred<R>): R {
        return call().await()
    }

    companion object {
        const val API_REQUEST_LOGGER = "API_REQUEST_LOGGER"
        const val TOKEN_QUERY = "access_token"
        const val token = "a0e6f6497e2c492dbd09e119a7340bd3"
    }
}