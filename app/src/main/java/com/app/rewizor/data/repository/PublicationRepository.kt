package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.CommonPublication

interface PublicationRepository {

    suspend fun fetchPublicationsList(
        page: Int,
        pageCount: Int,
        category: String? = null,
        pageType: String? = null
    ): RewizorResult<List<CommonPublication>>

    suspend fun fetchPublication(id: Long): RewizorResult<CommonPublication>
}