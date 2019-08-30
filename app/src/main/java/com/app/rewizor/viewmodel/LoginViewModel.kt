package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.exstension.isNullOrNorChars
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent
import org.koin.core.KoinComponent


class LoginViewModel(
    private val repository: LoginRepository
) : BaseViewModel(), KoinComponent {

    private lateinit var authorizationViewModel: AuthorizationViewModel

    private val enteredLogin: MutableLiveData<String> = MutableLiveData()
    val currentEnteredLogin: LiveData<String> get() = enteredLogin

    private val enteredPassword: MutableLiveData<String> = MutableLiveData()
    val currentEnteredPassword: LiveData<String> get() = enteredPassword

    private val validationInfo: MutableLiveData<List<VALIDATION>> = SingleLiveEvent()
    val validationInfoLiveData: LiveData<List<VALIDATION>> get() = validationInfo

    private val validationErrorModel: MutableLiveData<VALIDATION> = MutableLiveData()
    val onValidationErrorLiveData: LiveData<VALIDATION> get() = validationErrorModel

    private val loginFail: MutableLiveData<String> = SingleLiveEvent()
    val onLoginFailedLiveData: LiveData<String> = loginFail


    override fun onViewCreated() {

    }

    fun setSharedViewModel(vm: AuthorizationViewModel) {
        authorizationViewModel = vm
    }


    fun onLoginInput(str: String) {
        enteredLogin.value = str
        authorizationViewModel.currentLogin = str
    }

    fun onPasswordInput(str: String) {
        enteredPassword.value = str
    }

    fun onRecoverPasswordClicked() {
        authorizationViewModel.onRecover()
    }

    fun onRegistrationClicked() {
        authorizationViewModel.onRegistration()
    }

    fun loginRequest() {
        if (isValid()) {
            asyncProvider.startSuspend {
                val result = asyncProvider.executeBackGroundTask {
                    repository.login(enteredLogin.value!!, enteredPassword.value!!)
                }
                if (result.error != null) onLoginFail(result.error)
                else onLoginSuccess()
            }
        }
    }

    private fun onLoginFail(error: RewizorError) {
        loginFail.value = error.message
    }

    private fun onLoginSuccess() {
        authorizationViewModel.openMain.value = true
    }

    private fun isValid(): Boolean {
        val validationResults: MutableList<VALIDATION> = mutableListOf()
        enteredLogin.value.isNullOrNorChars { validationResults.add(VALIDATION.ENTER_LOGIN) }
        enteredPassword.value.isNullOrNorChars { validationResults.add(VALIDATION.ENTER_PASSWORD) }
        return validationResults.isEmpty()
    }

    enum class VALIDATION(val info: String) {
        ENTER_PASSWORD("Введите пароль"),
        ENTER_LOGIN("Введите логин"),
        SOME_ERROR("Ошибка")
    }
}