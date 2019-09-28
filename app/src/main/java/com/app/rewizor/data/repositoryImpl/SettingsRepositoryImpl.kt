package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.model.NotificationsModel
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SettingsRepository
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.remote.RestClient

class SettingsRepositoryImpl(
    private val prefs: PreferencesCache,
    private val apiClient: RestClient,
    private val accountRepository: AccountRepository
) : SettingsRepository {

    override suspend fun setSettings(account: Account): RewizorResult<Account> {
        val result = apiClient.run {
            with(account) {
                callApi {
                    api.updateSettings(
                        newsPushes,
                        materialsPushes,
                        favoritesPushes
                    )
                }
            }
        }
        return result.run {
            if (!isError) {
                data?.let {
                    accountRepository.account = it
                    prefs.notificationsModel = it.run {
                        NotificationsModel(
                            newsPushes,
                            materialsPushes,
                            favoritesPushes
                        )
                    }
                }
                RewizorResult(data!!)
            } else {
                map()
            }
        }
    }
}