package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.PageInfo
import com.app.rewizor.data.model.PublicationCommon
import com.app.rewizor.data.model.PublicationDetailed
import com.app.rewizor.data.repository.PublicationRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.exstension.getNotEmpty
import com.app.rewizor.remote.RestClient

class PublicationRepositoryImpl(
    private val apiClient: RestClient,
    private val systemRepository: SystemRepository
) : PublicationRepository {


    override suspend fun fetchPublicationsList(
        params: PublicationRepository.Params,
        filter: PublicationRepository.Filter?
    ): RewizorResult<List<PublicationCommon>> {
        val pageResult = params.run {
            apiClient.callApi {
                apiClient.api.getNodes(
                    page.toString(),
                    pageCount.toString(),
                    category,
                    pageType,
                    filter?.age.getNotEmpty(),
                    filter?.period.getNotEmpty(),
                    filter?.searchText.getNotEmpty(),
                    filter?.places?.getNotEmpty(),
                    filter?.popular,
                    filter?.recommendations
                )
            }
        }

        val page = pageResult.map(PageInfo.DEFAULT)
        page.model?.records?.forEach { record ->
            systemRepository.rewizorCategories
                .firstOrNull {
                    it.guid == record.category
                }
                ?.name
                ?.let { record.categoryTitle = it }
        }

        return RewizorResult(page.model?.records)

    }

    override suspend fun fetchPublication(id: String): RewizorResult<PublicationDetailed> {
        val pubResult = apiClient.run { callApi { api.getSpecificNode(id) } }
        return pubResult.map().also {
            if (!pubResult.isError) {
                it.model?.categoryView =
                    systemRepository.rewizorCategories.firstOrNull { c -> c.guid == it.model?.category }?.name
                        ?: ""
            }
        }
    }
}