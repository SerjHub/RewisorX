package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.remote.RestClient

class AccountRepositoryImpl(
    private val prefs: PreferencesCache,
    private val apiClient: RestClient
) : AccountRepository {
    override var account: Account = Account.DEFAULT
        set(value) {
            if (value.token.isNotEmpty()) prefs.sessionToken = value.token
            field = value
        }

    override suspend fun recoverPassword(email: String): RewizorResult<Unit> {
        val remoteResult = apiClient.run { callApi { api.recoverPassword(email) } }
        return remoteResult.map(Unit)
    }
}