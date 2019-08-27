package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.Region
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class RegionViewModel(
    private val systemRepo: SystemRepository,
    private val accountRepo: AccountRepository
) : BaseViewModel() {

    lateinit var mainViewModel: MainViewModel

    private val citiesList: MutableLiveData<List<Region>> = MutableLiveData()
    val citiesListLiveData: LiveData<List<Region>> get() = citiesList

    private val onCityChooseFail: MutableLiveData<Boolean> = SingleLiveEvent()
    val onCityChooseFailLiveDate: LiveData<Boolean> get() = onCityChooseFail

    override fun onViewCreated() {
        citiesList.value = systemRepo.regions
    }

    fun setParentViewModel(viewModel: MainViewModel) {
        mainViewModel = viewModel
    }

    fun searchInserted(value: String) {
        if (value.length > 1)
            citiesList.value = systemRepo.regions.filter { it.name.contains(value) }
        else if (citiesList.value?.size != systemRepo.regions.size) {
            citiesList.value = systemRepo.regions
        }

    }

    fun onCityClicked(id: Int) {
        with(asyncProvider) {
            startSuspend {
                val accountRes = accountRepo.run {
                    updateCity(account.copy(region = citiesList.value!!.find { it.id == id }))
                }
                if (accountRes.isError) {
                    onFail()
                } else {
                    onSuccess()
                }
            }
        }
    }

    private fun onFail() {
        onCityChooseFail.value = true
    }

    private fun onSuccess() {
        mainViewModel.cityChosen()
    }
}