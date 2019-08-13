package com.app.rewizor.viewmodel

import android.util.Log
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
    private val lastNameData: MutableLiveData<String> get() = lastName
    private val firstName: MutableLiveData<String> = MutableLiveData()
    private val firstNameData: MutableLiveData<String> get() = firstName
    private val email: MutableLiveData<String> = MutableLiveData()
    private val emailData: MutableLiveData<String> get() = email
    private val phone: MutableLiveData<String> = MutableLiveData()
    private val phoneData: MutableLiveData<String> get() = phone

    private val validationInfo: MutableLiveData<List<VALIDATION>> = SingleLiveEvent()
    val validationInfoLiveData: LiveData<List<VALIDATION>> get() = validationInfo

    private val registrationFail: MutableLiveData<String> = SingleLiveEvent()
    val registrationRequestFailedLiveData: LiveData<String> get() = registrationFail


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
        Log.i("FindViewM", "${lastName.value} ${firstName.value} ${email.value} ${phone.value}")
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