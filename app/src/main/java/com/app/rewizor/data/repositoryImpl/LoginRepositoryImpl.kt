package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.preferences.PreferencesCache

class LoginRepositoryImpl(
    private val preferences: PreferencesCache
) : LoginRepository {
    override val savedLogin
        get() = preferences.savedLogin ?: ""
}