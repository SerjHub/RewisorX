package com.app.rewizor.viewmodel

import com.app.rewizor.data.repository.AccountRepository

class SettingsViewModel(
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    override fun onViewCreated() {
        accountRepository.account
    }
}