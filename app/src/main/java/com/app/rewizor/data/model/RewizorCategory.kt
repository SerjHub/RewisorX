package com.app.rewizor.data.model

data class RewizorCategory(
    val guid: String,
    val name: String
) {
    companion object {
        val ALL = RewizorCategory("", "ВСЕ")
    }
}