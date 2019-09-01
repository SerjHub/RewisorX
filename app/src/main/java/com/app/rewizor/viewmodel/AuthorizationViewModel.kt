package com.app.rewizor.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.ui.utils.AUTHORIZATION
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class AuthorizationViewModel(
    private val accountRepository: AccountRepository,
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    private val mutableLogin: MutableLiveData<String> = MutableLiveData()
    var currentLogin
        get() =
            if (isEmailValid(mutableLogin.value))  ""
            else mutableLogin.value!!
        set(value) {
            mutableLogin.value = value
        }


    private val currentStartScreen: MutableLiveData<String> = MutableLiveData()
    val screen: LiveData<String> get() = currentStartScreen

    val openMain: MutableLiveData<Boolean> = SingleLiveEvent()

    override fun onViewCreated() {
        asyncProvider.startSuspend {

        }
    }

    fun onRecover() {
        currentStartScreen.value = AUTHORIZATION.RECOVER.name
    }

    fun onRegistration() {
        currentStartScreen.value = AUTHORIZATION.REGISTRATION.name
    }

    fun isEmailValid(email: String?) =
        email?.let {
            mutableLogin.value == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } ?: false

}