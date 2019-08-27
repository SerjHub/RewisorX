package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.CommonPublication

interface PublicationRepository {

    suspend fun getMainPublicationsList(
        page: Int,
        pageCount: Int,
        category: String? = null,
        pageType: String? = null
    ): RewizorResult<List<CommonPublication>>

    suspend fun getPublicationsList(
        page: Int,
        pageCount: Int,
        topic: String,
        category: String? = null
    ): RewizorResult<Unit>
}