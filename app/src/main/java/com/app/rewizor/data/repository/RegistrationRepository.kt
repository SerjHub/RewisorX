package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account

interface RegistrationRepository {
    suspend fun register(
        lastName: String,
        firstName: String,
        email: String,
        phone: String
    ): RewizorResult<Account>
}