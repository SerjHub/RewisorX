package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class StartViewModel(
    private val loginRepository: LoginRepository,
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    private val currentStartScreen: MutableLiveData<FRAGMENT> = MutableLiveData()
    val screen: LiveData<FRAGMENT> get() = currentStartScreen

    val openMain: MutableLiveData<Boolean> = SingleLiveEvent()

    override fun onViewCreated() {
        systemRepository.initApiClient()
        checkLogin()
    }

    private fun checkLogin() {
        currentStartScreen.value = FRAGMENT.LOGIN
//        if (loginRepository.savedLogin.isEmpty())
//            currentStartScreen.value = FRAGMENT.LOGIN
//        else openMain.value = true
    }

    enum class FRAGMENT() {
        LOGIN,
        REGISTRATION,
        RECOVER
    }
}