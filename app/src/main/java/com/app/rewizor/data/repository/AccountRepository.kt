package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.model.Region

interface AccountRepository {
    var account: Account
    val isAuthorized: Boolean
    val region: Region
    suspend fun getAccount(): RewizorResult<Account>
    suspend fun updateAccount(account: Account): RewizorResult<Account>
    suspend fun updateCity(region: Region): RewizorResult<Account>
    fun logout()

    var systemRegions: List<Region>
}