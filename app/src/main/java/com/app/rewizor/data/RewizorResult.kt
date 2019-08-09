package com.app.rewizor.data

data class RewizorResult<T>(
    val model: T,
    val error: RewizorError? = null
)