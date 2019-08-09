package com.app.rewizor.data.model

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
        val DEFAULT = Account(
            "",
            "",
            "",
            "",
            "",
            Region.DEFAULT,
            Avatar.DEFAULT,
            ""
        )
    }

    fun isAuthorized() = token.isNotEmpty()
}