package com.app.rewizor.data

data class RewizorResult<T>(
    val model: T? = null,
    val error: RewizorError? = null
) {
    val isError
        get() = error != null

    override fun toString(): String =
        "model = $model :: ${error?.let { "code = ${it.code} ::  message = ${it.message}"} ?: "no error"}"
}

fun <T>RewizorResult<T>.processWith(fail: (RewizorError) -> Unit, success: (T?) -> Unit): RewizorResult<T> {
    if (isError) fail.invoke(error ?: RewizorError.DEFAULT)
    else success.invoke(model)
    return this
}