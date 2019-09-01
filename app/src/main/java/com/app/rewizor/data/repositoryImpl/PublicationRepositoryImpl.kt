package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.CommonPublication
import com.app.rewizor.data.model.PageInfo
import com.app.rewizor.data.repository.PublicationRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.remote.RestClient

class PublicationRepositoryImpl(
    private val apiClient: RestClient,
    private val systemRepository: SystemRepository
) : PublicationRepository {

    override suspend fun fetchPublicationsList(
        page: Int,
        pageCount: Int,
        category: String?,
        pageType: String?
    ): RewizorResult<List<CommonPublication>> {
        val publicationsResult = apiClient.run { callApi { api.getNodes(
            page.toString(),
            pageCount.toString(),
            category,
            pageType
        ) } }
        val pageResult = publicationsResult.map(PageInfo.DEFAULT)
        pageResult.model.records.forEach { record ->
            systemRepository.rewizorCategories
                .firstOrNull {
                    it.guid == record.category }
                ?.name
                ?.let { record.categoryTitle = it }
        }

        return RewizorResult(pageResult.model.records)
    }

    override suspend fun fetchPublication(id: Long): RewizorResult<CommonPublication> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}