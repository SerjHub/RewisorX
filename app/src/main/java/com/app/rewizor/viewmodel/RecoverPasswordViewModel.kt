package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class RecoverPasswordViewModel(
    private val loginRepository: LoginRepository
) : BaseViewModel() {

    private lateinit var authorizationViewModel: AuthorizationViewModel

    private val insertedEmail: MutableLiveData<String> = MutableLiveData()

    private val currentLogin: MutableLiveData<String> = MutableLiveData()
    val insertedEmailLiveData: LiveData<String> get() = currentLogin

    private val invalidInput: MutableLiveData<String> = SingleLiveEvent()
    val invalidInputLiveData: LiveData<String> get() = invalidInput

    private val passwordRecovered: MutableLiveData<String> = MutableLiveData()
    val passwordRecoveredLiveData: LiveData<String> = passwordRecovered

    private val recoverFailedInfo: MutableLiveData<String> = MutableLiveData()
    val recoverFailedInfoLiveData: LiveData<String> get() = recoverFailedInfo

    fun onEmailInserted(email: String) {
        insertedEmail.value = email
    }

    fun setSharedVM(vm: AuthorizationViewModel) {
        authorizationViewModel = vm
     //   currentLogin.value = authorizationViewModel.currentLogin
    }

    fun onRecoverClicked() {

        if (authorizationViewModel.isEmailValid(insertedEmail.value)) return
        with(asyncProvider) {
            startSuspend {
                val result = executeBackGroundTask { loginRepository.recoverPassword(insertedEmail.value!!) }
                when { result.isError -> recoverFailed(result.error!!)
                       else -> recoverSuccess() }
            }
        }

    }

    private fun recoverFailed(error: RewizorError) {
        recoverFailedInfo.value = error.message
    }

    private fun recoverSuccess() {
        passwordRecovered.value = insertedEmail.value
    }

    override fun onViewCreated() {

    }

    companion object {
        const val INCORRECT_EMAIL = "Введите имейл в формате example@email.com"
    }
}
