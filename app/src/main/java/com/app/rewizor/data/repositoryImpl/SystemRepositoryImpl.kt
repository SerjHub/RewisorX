package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Region
import com.app.rewizor.data.model.RewizorCategory
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.remote.RestClient

class SystemRepositoryImpl (private val apiClient: RestClient): SystemRepository {
    override lateinit var rewizorCategories: List<RewizorCategory>
    override lateinit var regions: List<Region>

    override suspend fun coldStart(): RewizorResult<Boolean> {
        //init api
        apiClient.setEndpoint()

        val categoriesResult = apiClient.run { callApi { api.getCategories() } }
        if (categoriesResult.isError) return RewizorResult(false, RewizorError(message = "Ошибка получения категорий публикаций"))
        rewizorCategories = listOf(RewizorCategory.ALL).plus(categoriesResult.map(listOf()).model as MutableList)


        val regionsResult = apiClient.run { callApi { api.getRegions() } }
        if (regionsResult.isError) return RewizorResult(false, RewizorError(message = "Ошибка получения городов"))
        regions = regionsResult.map(listOf()).model

        return RewizorResult(true)
    }
}