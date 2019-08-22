package com.app.rewizor.data.model

data class ImageInfo(
    val guid: String?,
    val preview: String?,
    val url: String?
) {
    companion object {
        val DEFAULT = ImageInfo("", "", "")
    }
}