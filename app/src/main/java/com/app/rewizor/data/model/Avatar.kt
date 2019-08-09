package com.app.rewizor.data.model

data class Avatar(
    val guid: String,
    val preview: String,
    val url: String
) {
    companion object {
        val DEFAULT = Avatar("", "", "")
    }
}