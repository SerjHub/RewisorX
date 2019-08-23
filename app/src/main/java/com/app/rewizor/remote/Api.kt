package com.app.rewizor.remote

import com.app.rewizor.data.model.Account
import com.app.rewizor.data.model.PageInfo
import com.app.rewizor.data.model.Region
import com.app.rewizor.data.model.RewizorCategory
import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    companion object {
        //on launch
        const val API_CATEGORIES = "/api/nodes/categories"
        const val API_REGIONS = "/api/dictionaries/regions"


        //users
        const val API_LOGIN = "/api/users/login"
        const val API_REGISTRATION = "/api/users/registration"
        const val API_PROFILE = "/api/users/profile"
        const val API_RECOVER_PASSWORD = "/api/users/password"

        //nodes
        const val API_NODES = "api/nodes/"
    }

    @GET(API_CATEGORIES)
    fun getCategories(): Deferred<RewizorResponse<List<RewizorCategory>>>

    @GET(API_REGIONS)
    fun getRegions(): Deferred<RewizorResponse<List<Region>>>

    @POST(API_LOGIN)
    @FormUrlEncoded
    fun login(
        @Field("Email") login: String,
        @Field("Password") password: String
    ): Deferred<RewizorResponse<Account>>

    @POST(API_REGISTRATION)
    @FormUrlEncoded
    fun register(
        @Field("LastName") lastName: String,
        @Field("FirstName") firstName: String,
        @Field("Email") email: String,
        @Field("Phone") phone: String
    ): Deferred<RewizorResponse<Account>>

    @POST(API_PROFILE)
    fun getProfile():Deferred<RewizorResponse<Account>>

//    @POST(API_PROFILE)
//    fun updateProfile(
//        @Field("LastName") lastName: String,
//        @Field("FirstName") firstName: String,
//        @Field("FirstName") firstName: String,
//        @Field("Email") email: String,
//        @Field("Phone") phone: String
//    )

    @POST(API_RECOVER_PASSWORD)
    @FormUrlEncoded
    fun recoverPassword(
        @Field("Email") email: String
    ): Deferred<RewizorResponse<Unit>>

    @POST(API_NODES)
    @FormUrlEncoded
    fun getNodes(
        @Field("parent") category: String? = null
    ): Deferred<RewizorResponse<PageInfo>>

//    @GET(API_NODES)
//    fun getCategoryNodes():

}