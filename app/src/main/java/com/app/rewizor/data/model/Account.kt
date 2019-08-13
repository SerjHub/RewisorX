package com.app.rewizor.data.model

import com.app.rewizor.data.repositoryImpl.AccountRepositoryImpl

data class Account(
    val lastName: String,
    val firstName: String,
    val middleName: String,
    val email: String,
    val phone: String,
    val region: Region,
    val avatar: Avatar,
    val token: String
) {
    companion object {
        const val ANONYM_TOKEN = AccountRepositoryImpl.ANONYM_TOKEN
        val DEFAULT = Account(
            "",
            "",
            "",
            "",
            "",
            Region.DEFAULT,
            Avatar.DEFAULT,
            ANONYM_TOKEN
        )
    }
}