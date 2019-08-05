package com.app.rewizor.data.model

data class Account(
    val login: String,
    val email: String,
    val imagePath: String
) {
    companion object {
        val DEFAULT = Account(
            "",
            "",
            ""
        )
    }
}