package com.app.rewizor.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class MainViewModel(
    private val accountRepository: AccountRepository,
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    init {
        Log.i("ViewModel", "created")
    }

    private val anonModel: MutableLiveData<Boolean> = MutableLiveData()
    val anonModelLiveData: LiveData<Boolean> get() = anonModel

    private val profile: MutableLiveData<Account> = MutableLiveData()
    val profileLiveData: LiveData<Account> get() = profile

    private val onNavigationSettings: MutableLiveData<Boolean> = SingleLiveEvent()
    val onNavigationSettingsLiveEvent: LiveData<Boolean> get() = onNavigationSettings

    private val onLoadSettingsError: MutableLiveData<String> = SingleLiveEvent()
    val onLoadSettingsErrorLiveEvent: LiveData<String> get() = onLoadSettingsError

    override fun onViewCreated() {
        with(asyncProvider) {
            startSuspend {
                val start = systemRepository.coldStart()
                if (!start.isError) {
                    onNavigationSettings.value = true
                    setProfile()
                } else {
                    onLoadSettingsError.value = start.error?.message ?: "${start.error?.code}"
                }
            }
        }

    }

    private fun setProfile() {
        anonModel.value = !accountRepository.isAuthorized
        if (anonModel.value != true) {
            profile.value = accountRepository.account
        }
    }

    fun logout() {
        anonModel.value = true
        accountRepository.logout()
    }

}
