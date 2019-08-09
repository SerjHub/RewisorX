package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.repository.RegistrationRepository
import com.app.rewizor.exstension.isNullOrNorChars
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent
import okhttp3.internal.toImmutableList

class RegistrationViewModel(
    private val registrationRepository: RegistrationRepository,
    private val startViewModel: StartViewModel?
) : BaseViewModel() {
    private val lastName: MutableLiveData<String> = MutableLiveData()
    private val firstName: MutableLiveData<String> = MutableLiveData()
    private val email: MutableLiveData<String> = MutableLiveData()
    private val phone: MutableLiveData<String> = MutableLiveData()

    private val validationInfo: MutableLiveData<List<VALIDATION>> = SingleLiveEvent()
    val validationInfoLiveData: LiveData<List<VALIDATION>> get() = validationInfo

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
        val validationResults: MutableList<VALIDATION> = mutableListOf()
        lastName.value.isNullOrNorChars { validationResults.add(VALIDATION.LASTNAME) }
        firstName.value.isNullOrNorChars { validationResults.add(VALIDATION.FIRSTNAME) }
        email.value.isNullOrNorChars { validationResults.add(VALIDATION.EMAIL) }
        phone.value.isNullOrNorChars { validationResults.add(VALIDATION.PHONE) }
        if (validationResults.isNotEmpty()) validationInfo.value = validationResults.toImmutableList()
        return validationResults.isEmpty()

    }

    enum class VALIDATION(val info: String) {
        LASTNAME("Введите фамилию"),
        FIRSTNAME("Введите имя"),
        EMAIL("Введите почту"),
        PHONE("Введите телефон")
    }
}