package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account

interface LoginRepository {
    val savedLogin: String
    suspend fun login(login: String, password: String): RewizorResult<Account>
    suspend fun recoverPassword(email: String): RewizorResult<Unit>
}