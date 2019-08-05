package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.LoginRepository

class LoginViewModel(
    private val viewModel: StartViewModel?,
    private val repository: LoginRepository
) : BaseViewModel() {

    private val enteredLogin: MutableLiveData<String> = MutableLiveData()
    val currentEnteredLogin: LiveData<String> get() = enteredLogin

    private val enteredPassword: MutableLiveData<String> = MutableLiveData()
    val currentEnteredPassword: LiveData<String> get() = enteredPassword

    private val validationErrorModel: MutableLiveData<VALIDATION> = MutableLiveData()
    val onValidationErrorLiveData: LiveData<VALIDATION> get() = validationErrorModel


    override fun onViewCreated() {

    }


    fun onLoginInput(str: String) {
        enteredLogin.value = str
    }

    fun onPasswordInput(str: String) {
        enteredPassword.value = str
    }

    private fun loginRequest() {
        if (isValid()) {
            asyncProvider.startSuspend {
                val result = asyncProvider.startBlockingOperationWithResultAsyncAwait {
                    repository.login(enteredLogin.value!!, enteredPassword.value!!)
                }
                if (result.error != null) {

                }
                else onLoginSuccess()
            }

        }

    }

    private fun onLoginSuccess() {
        viewModel?.openMain?.value = true
    }

    private fun isValid(): Boolean {
        when {
            enteredLogin.value == null -> validationErrorModel.value = VALIDATION.ENTER_LOGIN
            enteredLogin.value!!.length < 4 -> validationErrorModel.value = VALIDATION.SHORT_LOGIN
            enteredPassword.value == null -> validationErrorModel.value = VALIDATION.ENTER_PASSWORD
            enteredPassword.value!!.length < 4 -> validationErrorModel.value = VALIDATION.SHORT_PASSWORD
            else -> return true
        }
        return false
    }

    enum class VALIDATION(val info: String) {
        SHORT_PASSWORD("Слишком короткий пароль"),
        ENTER_PASSWORD("Введите пароль"),
        SHORT_LOGIN("Слишком короткий логин"),
        ENTER_LOGIN("Введите логин"),
        SOME_ERROR("Ошибка")
    }
}