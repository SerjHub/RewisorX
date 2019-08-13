package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult

interface PublicationRepository {
    fun getPublicationsList(
        page: Int,
        pageCount: Int,
        topic: String,
        category: String
    ): RewizorResult<Unit>
}