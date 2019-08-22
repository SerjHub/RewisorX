package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.ui.utils.AUTHORIZATION
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class StartViewModel(
    private val accountRepository: AccountRepository,
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    private val currentStartScreen: MutableLiveData<String> = MutableLiveData()
    val screen: LiveData<String> get() = currentStartScreen

    val openMain: MutableLiveData<Boolean> = SingleLiveEvent()

    override fun onViewCreated() {

    }

    fun onRecover() {
        currentStartScreen.value = AUTHORIZATION.RECOVER.name
    }

    fun onRegistration() {
        currentStartScreen.value = AUTHORIZATION.REGISTRATION.name
    }
}