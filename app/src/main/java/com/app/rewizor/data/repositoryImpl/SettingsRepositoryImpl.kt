package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.repository.SettingsRepository
import com.app.rewizor.preferences.PreferencesCache

class SettingsRepositoryImpl(
    private val prefs: PreferencesCache
) : SettingsRepository {

}