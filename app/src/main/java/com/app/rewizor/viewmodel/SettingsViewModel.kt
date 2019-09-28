package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SettingsRepository
import com.app.rewizor.exstension.asyncRequest

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    private val account: MutableLiveData<Account> = MutableLiveData()
    val accountLiveData: LiveData<Account> get() = account

    private val hasAccount: MutableLiveData<Boolean> = MutableLiveData()
    val hasAccountLiveData: LiveData<Boolean> get() = hasAccount

    override fun onViewCreated() {
        account.value = accountRepository.account
    }

    fun notificationChecked(
        news: Boolean? = null,
        articles: Boolean? = null,
        favorites: Boolean? = null
    ) {
        account.value?.let {

            val newAccount = it.copy(
                newsPushes = news ?: it.newsPushes,
                materialsPushes = articles ?: it.materialsPushes,
                favoritesPushes = favorites ?: it.favoritesPushes
            )
            asyncRequest(
                loadData = {
                    settingsRepository.setSettings(
                        newAccount
                    )
                },
                onFail = { _ ->
                    account.value = it
                },
                onSuccess = { account.value = newAccount }
            )
        }
    }

}