package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.LoginRepository

class StartViewModel(
    private val repository: LoginRepository
) : BaseViewModel() {

    private val currentStartScreen: MutableLiveData<StartView> = MutableLiveData()
    val screen: LiveData<StartView> get() = currentStartScreen

    val openMain: MutableLiveData<Boolean> = MutableLiveData()

    override fun onViewCreated() {
        checkLogin()
    }

    private fun checkLogin() {
        if (repository.savedLogin.isEmpty())
            currentStartScreen.value = StartView.LOGIN
        else openMain.value = true
    }

    enum class StartView() {
        LOGIN,
        REGISTRATION,
        RECOVER
    }
}