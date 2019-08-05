package com.app.rewizor.data.repository

import com.app.rewizor.data.Result
import com.app.rewizor.data.model.Account

interface LoginRepository {
    val savedLogin: String
    suspend fun login(login: String, password: String): Result<Account>
}