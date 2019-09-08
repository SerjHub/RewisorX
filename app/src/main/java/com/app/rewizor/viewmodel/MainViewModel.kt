package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.model.Region
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.exstension.asyncRequest
import com.app.rewizor.ui.utils.TOPIC
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class MainViewModel(
    private val accountRepository: AccountRepository,
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    private val anonModel: MutableLiveData<Boolean> = MutableLiveData()
    val anonModelLiveData: LiveData<Boolean> get() = anonModel

    private val regionModel: MutableLiveData<String> = MutableLiveData()
    val regionModelLiveData: LiveData<String> get() = regionModel

    private val profile: MutableLiveData<Account> = MutableLiveData()
    val profileLiveData: LiveData<Account> get() = profile

    private val onNavigationSettings: MutableLiveData<Boolean> = SingleLiveEvent()
    val onNavigationSettingsLiveEvent: LiveData<Boolean> get() = onNavigationSettings

    private val onTopicChosen: MutableLiveData<TOPIC> = MutableLiveData()
    val onTopicChosenLiveData: LiveData<TOPIC> get() = onTopicChosen

    private val cityFilterOpened: MutableLiveData<Region> = SingleLiveEvent()
    val cityFilterOpenedLiveData: LiveData<Region> get() = cityFilterOpened

    private val aboutOpened: MutableLiveData<Boolean> = SingleLiveEvent()
    val aboutOpenedLiveData: LiveData<Boolean> get() = aboutOpened

    private val supportOpened: MutableLiveData<Boolean> = SingleLiveEvent()
    val supportOpenedLiveData: LiveData<Boolean> get() = supportOpened

    private val contentShowing: MutableLiveData<Boolean> = MutableLiveData()
    val contentShowingLiveData: LiveData<Boolean> get() = contentShowing

    private val onLoadSettingsError: MutableLiveData<String> = SingleLiveEvent()
    val onLoadSettingsErrorLiveEvent: LiveData<String> get() = onLoadSettingsError

    override fun onViewCreated() {
        asyncRequest(
            loadData = { systemRepository.coldStart() },
            onFail = { onLoadSettingsError.value = it.message },
            onSuccess = { systemLoaded() }
        )
    }

    private fun systemLoaded() {
        onNavigationSettings.value = true
        onTopicChosen.value = TOPIC.MAIN
        setProfile()
    }

    fun refreshData() {
        onTopicChosen.value = TOPIC.MAIN
        setProfile()
    }

    fun menuClicked(topic: TOPIC) {
        if (onTopicChosen.value == topic) return
        onTopicChosen.value = topic
    }

    fun openLastTopic() {
        onTopicChosen.value = onTopicChosen.value
    }



    fun cityChosen() {
        contentShowing.value = true
        if (regionModel.value != accountRepository.region.name) {
            regionModel.value = accountRepository.region.name
        }
    }

    fun cityClicked() {
        cityFilterOpened.value = accountRepository.region
    }

    fun aboutClicked() {
        aboutOpened.value = true
    }

    fun supportClicked() {
        supportOpened.value = true
    }

    fun onBackClicked() {
        when {
            supportOpened.value == true ||
                    aboutOpened.value == true -> {

            }
            contentShowing.value != true -> { contentShowing.value = true }
        }
    }

    private fun setProfile() {
        if (cityFilterOpened.value == null) {
            regionModel.value = accountRepository.region.name
            anonModel.value = !accountRepository.isAuthorized
            if (anonModel.value != true) {
                profile.value = accountRepository.account
            }
        }
    }

    fun logout() {
        anonModel.value = true
        accountRepository.logout()
    }

}
