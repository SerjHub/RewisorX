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

    private val currentTopic: MutableLiveData<TOPIC> = MutableLiveData()
    val currentTopicLiveData: LiveData<TOPIC> get() = currentTopic

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

    private val filterOpened: MutableLiveData<Boolean> = MutableLiveData()
    val filterOpenedLiveData: LiveData<Boolean> get() = filterOpened

    private val filterEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val filterEnabledLiveData: LiveData<Boolean> get() = filterEnabled

    private val filterVisible: MutableLiveData<Boolean> = MutableLiveData()
    val filterVisibleLiveData: LiveData<Boolean> get() = filterVisible

    override fun onViewCreated() {
        asyncRequest(
            loadData = { systemRepository.coldStart() },
            onFail = { onLoadSettingsError.value = it.message },
            onSuccess = { systemLoaded() }
        )
    }

    private fun systemLoaded() {
        currentTopic.value = TOPIC.MAIN
        setProfile()
    }

    fun refreshData() {
        currentTopic.value = TOPIC.MAIN
        setProfile()
    }

    fun menuClicked(topic: TOPIC) {
        if (currentTopic.value == topic) return
        currentTopic.value = topic
        filterEnabled.value = topic.filters?.isCleared()?.not() ?: false
        filterVisible.value = topic != TOPIC.MAIN
    }

    fun openLastTopic() {
        currentTopic.value = currentTopic.value
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

    private fun setProfile() {
        if (cityFilterOpened.value == null) {
            regionModel.value = accountRepository.region.name
            anonModel.value = !accountRepository.isAuthorized
            if (anonModel.value != true) {
                profile.value = accountRepository.account
            }
        }
    }

    fun filterClicked() {
        filterOpened.value = filterOpened.value?.not() ?: true
    }

    fun filterEnabled(enabled: Boolean) {
        filterEnabled.value = enabled
    }

    fun logout() {
        anonModel.value = true
        accountRepository.logout()
        onCleared()
    }

    fun clearVm() {
        onCleared()
    }
}
