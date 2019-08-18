package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class StartViewModel(
    private val accountRepository: AccountRepository,
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    private val currentStartScreen: MutableLiveData<FRAGMENT> = MutableLiveData()
    val screen: LiveData<FRAGMENT> get() = currentStartScreen

    val openMain: MutableLiveData<Boolean> = SingleLiveEvent()

    override fun onViewCreated() {
        with(asyncProvider) {
            startSuspend {
                val start = systemRepository.coldStart()
                if (!start.isError) checkLogin()
            }
        }
    }

    fun onRecover() {
        currentStartScreen.value = FRAGMENT.RECOVER
    }

    fun onRegistration() {
        currentStartScreen.value = FRAGMENT.REGISTRATION
    }

    private fun checkLogin() {

        if (accountRepository.isAuthorized) openMain.value = true
        else currentStartScreen.value = FRAGMENT.LOGIN
    }


    enum class FRAGMENT {
        LOGIN,
        REGISTRATION,
        RECOVER
    }
}