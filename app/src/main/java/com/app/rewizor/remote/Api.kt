package com.app.rewizor.remote

import com.app.rewizor.data.model.Account
import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    companion object {
        const val API_LOGIN = "/api/users/login/"
        const val API_REGISTRATION = "/api/users/registration/"
        const val API_PROFILE = "/api/users/profile/"
        const val API_RECOVER_PASSWORD = "/api/users/password/"
    }

    @POST(API_LOGIN)
    @FormUrlEncoded
    fun login(
        @Field("Email") login: String,
        @Field("Password") password: String
    ): Deferred<RewizorResponse<Account>>
}