package com.app.rewizor.data.model

data class PageInfo(
    val total: Int,
    val pages: Int,
    val page: Int,
    val records: List<PublicationCommon>
) {
    companion object {
        val DEFAULT = PageInfo(0, 0, 0, emptyList())
    }
}