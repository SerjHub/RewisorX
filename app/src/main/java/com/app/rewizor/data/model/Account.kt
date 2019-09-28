package com.app.rewizor.data.model

import com.app.rewizor.data.repositoryImpl.AccountRepositoryImpl

data class Account(
    val lastName: String?,
    val firstName: String?,
    val middleName: String?,
    val email: String?,
    val phone: String?,
    val newsPushes: Boolean,
    val materialsPushes: Boolean,
    val favoritesPushes: Boolean,
    val region: Region?,
    val avatar: ImageInfo?,
    val accessToken: String?
) {
    companion object {
        const val ANONYM_TOKEN = AccountRepositoryImpl.ANON_TOKEN
        val DEFAULT = Account(
            "",
            "",
            "",
            "",
            "",
            false,
            false,
            false,
            Region.DEFAULT,
            ImageInfo.DEFAULT,
            ANONYM_TOKEN
        )
    }
}