package com.app.rewizor.data

data class RewizorResult<T>(
    val model: T,
    val error: RewizorError? = null
) {
    val isError
        get() = error != null

    override fun toString(): String =
        "model = $model :: ${error?.let { "code = ${it.code} ::  message = ${it.message}"} ?: "no error"}"
}