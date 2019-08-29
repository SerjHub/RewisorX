package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.exstension.isNullOrNorChars
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class LoginViewModel(
    private val parentViewModel: StartViewModel?,
    private val repository: LoginRepository
) : BaseViewModel() {

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


    fun onLoginInput(str: String) {
        enteredLogin.value = str
    }

    fun onPasswordInput(str: String) {
        enteredPassword.value = str
    }

    fun onRecoverPasswordClicked() {
        parentViewModel?.onRecover()
    }

    fun onRegistrationClicked() {
        parentViewModel?.onRegistration()
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
        parentViewModel?.openMain?.value = true
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