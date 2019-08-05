package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.Result
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.preferences.PreferencesCache

class LoginRepositoryImpl(
    private val preferences: PreferencesCache
) : LoginRepository {
    override val savedLogin
        get() = preferences.savedLogin ?: ""

    override suspend fun login(login: String, password: String): Result<Account> {
        return Result(Account.DEFAULT)
    }


}