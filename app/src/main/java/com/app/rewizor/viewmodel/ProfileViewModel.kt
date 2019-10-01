package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.exstension.asyncRequest

class ProfileViewModel(
    private val accountRepository: AccountRepository
) : BaseViewModel() {
    var mainViewModel: MainViewModel? = null

    private val account: MutableLiveData<Account> = MutableLiveData()
    val accountLiveData: LiveData<Account> get() = account

    private val saveError: MutableLiveData<Boolean> = MutableLiveData()
    val saveErrorLiveData: LiveData<Boolean> get() = saveError

    override fun onViewCreated() {
        account.value = accountRepository.account
    }

    fun onFirstNameInserted(string: String) {
        account.value?.let {
            account.value = it.copy(firstName = string)
        }
    }

    fun onMiddleNameInserted(string: String) {
        account.value?.let {
            account.value = it.copy(middleName = string)
        }
    }

    fun onLastNameInserted(string: String) {
        account.value?.let {
            account.value = it.copy(lastName = string)
        }
    }

    fun onEmailNameInserted(string: String) {
        account.value?.let {
            account.value = it.copy(email = string)
        }
    }

    fun onPhoneNameInserted(string: String) {
        account.value?.let {
            account.value = it.copy(phone = string)
        }
    }

    fun onSaveClicked() {
        account.value?.let {
            asyncRequest(
                loadData = { accountRepository.updateAccount(it)},
                onFail = { saveError.value = true },
                onSuccess = { mainViewModel!!.openLastTopic() })
        }
    }

    fun onLogout() {
        mainViewModel!!.logout()
        mainViewModel!!.openLastTopic()
    }

}