package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.PublicationCommon
import com.app.rewizor.data.model.PublicationDetailed

interface PublicationRepository {

    suspend fun fetchPublicationsList(
        page: Int,
        pageCount: Int,
        category: String? = null,
        pageType: String? = null
    ): RewizorResult<List<PublicationCommon>>

    suspend fun fetchPublication(id: String): RewizorResult<PublicationDetailed>
}