package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.repository.RegistrationRepository
import com.app.rewizor.exstension.isNullOrNorChars
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent
import okhttp3.internal.toImmutableList

class RegistrationViewModel(
    private val registrationRepository: RegistrationRepository
) : BaseViewModel() {

    private lateinit var authorizationViewModel: AuthorizationViewModel

    private val lastName: MutableLiveData<String> = MutableLiveData()
    private val firstName: MutableLiveData<String> = MutableLiveData()
    private val email: MutableLiveData<String> = MutableLiveData()
    private val phone: MutableLiveData<String> = MutableLiveData()

    private val validationInfo: MutableLiveData<List<VALIDATION>> = SingleLiveEvent()
    val validationInfoLiveData: LiveData<List<VALIDATION>> get() = validationInfo

    private val registrationFail: MutableLiveData<String> = SingleLiveEvent()
    val registrationRequestFailedLiveData: LiveData<String> get() = registrationFail

    private val agreementAccepted: MutableLiveData<Boolean> = MutableLiveData()


    fun setLast(txt: String){
        lastName.value = txt
    }
    fun setFirst(txt: String){
        firstName.value = txt
    }
    fun setEmail(txt: String){
        email.value = txt
    }
    fun setPhone(txt: String){
        phone.value = txt
    }
    fun setAgreement(accepted: Boolean) {
        agreementAccepted.value = accepted
    }


    override fun onViewCreated() {

    }

    fun setSharedVM(vm: AuthorizationViewModel) {
        authorizationViewModel = vm
    }

    fun onRegistrationClicked() {
        if (isValid()) {
            asyncProvider.startSuspend {
                val result = asyncProvider.executeBackGroundTask { registrationRepository.register(
                    lastName.value!!,
                    firstName.value!!,
                    email.value!!,
                    phone.value!!
                ) }
                if (result.isError) onRegistrationFail(result.error!!)
                else onRegistrationSuccess()
            }
        }
    }

    private fun onRegistrationSuccess() {
        authorizationViewModel.openMain.value = true
    }

    private fun onRegistrationFail(error: RewizorError) {
        registrationFail.value = error.message
    }

    private fun isValid(): Boolean {
        val validationResults: MutableList<VALIDATION> = mutableListOf()
        lastName.value.isNullOrNorChars { validationResults.add(VALIDATION.LASTNAME) }
        firstName.value.isNullOrNorChars { validationResults.add(VALIDATION.FIRSTNAME) }
        email.value.also {
            if (!authorizationViewModel.isEmailValid(it)) {
                validationResults.add(VALIDATION.EMAIL)
            }
        }


        phone.value.isNullOrNorChars { validationResults.add(VALIDATION.PHONE) }
        if (agreementAccepted.value != true) validationResults.add(VALIDATION.AGREEMENT)
        if (validationResults.isNotEmpty())  validationInfo.value = validationResults.toImmutableList()
        return validationResults.isEmpty()

    }

    enum class VALIDATION(val info: String) {
        LASTNAME("Введите фамилию"),
        FIRSTNAME("Введите имя"),
        EMAIL("Введите корректную почту"),
        PHONE("Введите телефон"),
        AGREEMENT("Требуется пользовательское оглашение")
    }
}