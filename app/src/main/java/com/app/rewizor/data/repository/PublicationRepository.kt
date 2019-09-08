package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.PublicationCommon
import com.app.rewizor.data.model.PublicationDetailed

interface PublicationRepository {

    suspend fun fetchPublicationsList(
        params: Params
    ): RewizorResult<List<PublicationCommon>>

    suspend fun fetchPublication(id: String): RewizorResult<PublicationDetailed>

    data class Params(
        val page: Int,
        val pageCount: Int,
        val category: String? = null,
        val pageType: String? = null,
        val age: Int? = null,
        val period: String? = null,
        val searchText: String? = null,
        val placesList: List<String>? = null,
        val popular: Boolean? = null,
        val recommendations: Boolean? = null

    )
}