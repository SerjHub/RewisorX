package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account

interface AccountRepository {
    var account: Account
    val isAuthorized: Boolean
    suspend fun getAccount(): RewizorResult<Account>
    suspend fun updateAccount(account: Account): RewizorResult<Account>
    suspend fun updateCity(account: Account): RewizorResult<Account>
    fun logout()
}