package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account

interface SettingsRepository {
    suspend fun setSettings(account: Account): RewizorResult<Account>
}