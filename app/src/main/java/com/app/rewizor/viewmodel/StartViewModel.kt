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

    private val currentStartScreen: MutableLiveData<StartView> = MutableLiveData()
    val screen: LiveData<StartView> get() = currentStartScreen

    val openMain: MutableLiveData<Boolean> = SingleLiveEvent()

    override fun onViewCreated() {
        systemRepository.initApiClient()
        checkLogin()
    }

    private fun checkLogin() {
        currentStartScreen.value = StartView.LOGIN
//        if (loginRepository.savedLogin.isEmpty())
//            currentStartScreen.value = StartView.LOGIN
//        else openMain.value = true
    }

    enum class StartView() {
        LOGIN,
        REGISTRATION,
        RECOVER
    }
}