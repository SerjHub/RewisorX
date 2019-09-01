package com.app.rewizor.data

data class RewizorError(
    val code: Int = INTERNAL_ERROR_STATUS,
    val message: String
) {
    companion object {
        const val INTERNAL_ERROR_STATUS = 100
        val DEFAULT = RewizorError(message = "Неизвестная ошибка", code =  100)
    }
}