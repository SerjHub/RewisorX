package com.app.rewizor.remote

import com.app.rewizor.data.model.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface Api {

    companion object {
        //on launch
        const val API_CATEGORIES = "/api/nodes/categories"
        const val API_REGIONS = "/api/dictionaries/regions"


        //users
        const val API_LOGIN = "/api/users/login"
        const val API_REGISTRATION = "/api/users/registration"
        const val API_GET_PROFILE = "/api/users/profile"
        const val API_UPDATE_PROFILE = "/api/users/update"
        const val API_RECOVER_PASSWORD = "/api/users/password"
        const val API_SETTINGS= "/api/users/settings/"
        const val API_PUSH_TOKEN= "/api/users/push-token/"

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

    @GET(API_GET_PROFILE)
    fun getProfile(): Deferred<RewizorResponse<Account>>

    @POST(API_UPDATE_PROFILE)
    @FormUrlEncoded
    fun updateProfile(
        @Field("LastName") lastName: String,
        @Field("FirstName") firstName: String,
        @Field("MiddleName")middleName: String,
        @Field("Email")email: String,
        @Field("Phone")phone: String,
        @Field("Region")region: Int?
    ): Deferred<RewizorResponse<Account>>

    @POST(API_RECOVER_PASSWORD)
    @FormUrlEncoded
    fun recoverPassword(
        @Field("Email") email: String
    ): Deferred<RewizorResponse<Unit>>

    @POST(API_NODES)
    @FormUrlEncoded
    fun getNodes(
        @Field("page") page: String,
        @Field("pageSize") pageSize: String,
        @Field("parent") category: String? = null,
        @Field("pageType") pageType: String? = null,
        @Field("age") age: String? = null,
        @Field("period") period: String? = null,
        @Field("text") text: String? = null,
        @Field("places") places: String? = null,
        @Field("popular") popular: Boolean? = null,
        @Field("recommendations") recommendations: Boolean? = null
    ): Deferred<RewizorResponse<PageInfo>>

    @POST("$API_NODES/{guid}")
    fun getSpecificNode(
        @Path("guid") guid: String
    ): Deferred<RewizorResponse<PublicationDetailed>>

    @POST(API_SETTINGS)
    @FormUrlEncoded
    fun updateSettings(
        @Field("NewsPushes") news: Boolean,
        @Field("MaterialsPushes") articles: Boolean,
        @Field("FavoritesPushes") favorite: Boolean
    ): Deferred<RewizorResponse<Account>>

    @POST(API_PUSH_TOKEN)
    fun updatePushToken(
        @Field("Token") token: String
    ): Deferred<RewizorResponse<Account>>
}

/**
 age - num,
 period - dateTime.string-dt.s (str),
 text - string (строка поиска),
 places - guid,guid.... (str)
 popular - boolean,
 recommendations - boolean
        */