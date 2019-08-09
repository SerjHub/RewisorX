package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.remote.RestClient

class SystemRepositoryImpl (private val apiClient: RestClient): SystemRepository {
    override fun initApiClient() = apiClient.setEndpoint()
}