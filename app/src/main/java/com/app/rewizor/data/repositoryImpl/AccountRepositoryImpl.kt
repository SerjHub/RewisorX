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
    override suspend fun updateAccount(account: Account): RewizorResult<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isAuthorized: Boolean
        get() = prefs.sessionToken != Account.ANONYM_TOKEN

    override var account: Account = Account.DEFAULT
        set(value) {
            updateAccountSettings(value)
            field = value
        }

    private fun updateAccountSettings(account: Account) {
        prefs.sessionToken = account.accessToken
        account.region?.id?.let { apiClient.region = it }
    }



    override suspend fun getAccount(): RewizorResult<Account> {
        val accountResult = apiClient.run { callApi { api.getProfile() } }
        return accountResult.map(Account.DEFAULT).also {
            if (!it.isError) account = it.model
        }
    }

    companion object {
        const val ANON_TOKEN = "a0e6f6497e2c492dbd09e119a7340bd3"
    }
}