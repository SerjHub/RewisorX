package com.app.rewizor.data

data class Error(
    val code: Int,
    val message: String,
    val reason: Int? = null
)