package com.app.rewizor.data.repositoryImpl

import android.util.Log
import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.RegistrationRepository
import com.app.rewizor.remote.RestClient

class RegistrationRepositoryImpl(
    private val apiClient: RestClient,
    private val accountRepository: AccountRepository): RegistrationRepository {

    override suspend fun register(lastName: String, firstName: String, email: String, phone: String): RewizorResult<Account> {
        val remoteResult = apiClient.run { callApi { api.register(lastName, firstName, email, phone) } }
        Log.i("FindResp", "${remoteResult}")
        return remoteResult.run {
            map(Account.DEFAULT)
                .also {
                    if (!isError) accountRepository.account = it.model
                }
        }
    }
}