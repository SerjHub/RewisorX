package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.remote.RestClient

class LoginRepositoryImpl(
    private val preferences: PreferencesCache,
    private val apiClient: RestClient,
    private val accountRepository: AccountRepository
) : LoginRepository {
    override val savedLogin
        get() = preferences.savedLogin ?: ""

    override suspend fun login(login: String, password: String): RewizorResult<Account> {
        val remoteResult = apiClient.run { callApi { api.login(login, password) } }
        return remoteResult.run {
            map(Account.DEFAULT)
                .also { if (!isError()) accountRepository.account = it.model }
        }
    }


}