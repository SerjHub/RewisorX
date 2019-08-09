package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.remote.RestClient

class LoginRepositoryImpl(
    private val preferences: PreferencesCache,
    private val apiClient: RestClient
) : LoginRepository {
    override val savedLogin
        get() = preferences.savedLogin ?: ""

    override suspend fun login(login: String, password: String): RewizorResult<Account> {
        val remoteResult = apiClient.run { callApi { api.login(login, password) } }
        return remoteResult.map(Account.DEFAULT)
    }


}