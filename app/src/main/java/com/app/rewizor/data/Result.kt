package com.app.rewizor.data

data class Result<T>(
    val model: T,
    val error: Error
)