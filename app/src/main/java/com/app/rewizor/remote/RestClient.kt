package com.app.rewizor.remote

import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repositoryImpl.AccountRepositoryImpl
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RestClient(private val baseUrl: String) : KoinComponent {
    lateinit var api: Api

    private val accountRepo: AccountRepository by inject()
    private val accessToken: String
        get() = accountRepo.account.token

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(createLogger())
        addInterceptor {  chain ->
            val request = chain.request()
            val urlBuilder = request.url.newBuilder()
            urlBuilder.addQueryParameter(TOKEN_QUERY, AccountRepositoryImpl.ANONYM_TOKEN)
  //          urlBuilder.addQueryParameter(TOKEN_QUERY, accessToken)
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

    private fun createLogger() =
        HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }



    companion object {
        val TAG = RestClient::class.java.name
        const val API_REQUEST_LOGGER = "API_REQUEST_LOGGER"
        const val TOKEN_QUERY = "access_token"
    }
}