package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.AccountRepository

class SupportViewModel(
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    private val currentEmail: MutableLiveData<Pair<String?, Boolean>> = MutableLiveData()
    val currentEmailLiveData: LiveData<Pair<String?, Boolean>> get() = currentEmail

    private val currentMessage: MutableLiveData<String> = MutableLiveData()
    val currentMessageLiveData: LiveData<String> get() = currentMessage

    override fun onViewCreated() {
        currentEmail.value = accountRepository.run {
            Pair(
                if (isAuthorized) account.email
                else null,
                true
            )
        }
        currentEmail.value = Pair(accountRepository.account.email, true)

    }

    fun emailInserted(text: String) {
        currentEmail.value = Pair(text, false)
    }

    fun messageInserted(text: String) {
        currentMessage.value = text
    }
}