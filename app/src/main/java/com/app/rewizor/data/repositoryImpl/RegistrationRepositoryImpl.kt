package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.RegistrationRepository
import com.app.rewizor.remote.RestClient

class RegistrationRepositoryImpl(private val apiClient: RestClient): RegistrationRepository {
    override suspend fun register(lastName: String, firstName: String, email: String, phone: String): RewizorResult<Account> {
        val remoteResult = apiClient.run { callApi { api.register(lastName, firstName, email, phone) } }
        return remoteResult.map(Account.DEFAULT)
    }
}