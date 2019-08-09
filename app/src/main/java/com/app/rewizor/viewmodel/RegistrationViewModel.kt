package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.repository.RegistrationRepository
import com.app.rewizor.exstension.isNullOrNorChars
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class RegistrationViewModel(
    private val registrationRepository: RegistrationRepository,
    private val startViewModel: StartViewModel?
) : BaseViewModel() {
    private val lastName: MutableLiveData<String> = MutableLiveData()
    private val firstName: MutableLiveData<String> = MutableLiveData()
    private val email: MutableLiveData<String> = MutableLiveData()
    private val phone: MutableLiveData<String> = MutableLiveData()

    private val validationInfo: MutableLiveData<VALIDATION> = SingleLiveEvent()
    val validationInfoLiveData: LiveData<VALIDATION> get() = validationInfo

    private val registrationFail: MutableLiveData<String> = SingleLiveEvent()
    val registrationRequestFailedLiveData: LiveData<String> get() = registrationFail


    override fun onViewCreated() {

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
        startViewModel?.openMain?.value = true
    }

    private fun onRegistrationFail(error: RewizorError) {
        registrationFail.value = error.message
    }

    private fun isValid(): Boolean {
        when {
            lastName.value.isNullOrNorChars() -> validationInfo.value = VALIDATION.LASTNAME
            firstName.value.isNullOrNorChars() -> validationInfo.value = VALIDATION.FIRSTNAME
            email.value.isNullOrNorChars() -> validationInfo.value = VALIDATION.EMAIL
            phone.value.isNullOrNorChars() -> validationInfo.value = VALIDATION.PHONE
            else -> return true
        }
        return false
    }

    enum class VALIDATION(val info: String) {
        LASTNAME("Введите фамилию"),
        FIRSTNAME("Введите имя"),
        EMAIL("Введите почту"),
        PHONE("Введите телефон")
    }
}