package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.ui.model.FragmentParamsModel
import com.app.rewizor.ui.utils.FilterStateModel
import com.app.rewizor.ui.utils.TOPIC

class TopicViewModel(
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    private val fragmentsTopic: MutableLiveData<TOPIC> = MutableLiveData()
    val fragmentsTopicLiveData: LiveData<TOPIC> get() = fragmentsTopic

    private val fragmentParamsModel: MutableLiveData<List<FragmentParamsModel>> = MutableLiveData()
    val fragmentParamsModelLiveData: LiveData<List<FragmentParamsModel>> get() = fragmentParamsModel

    private val filter: MutableLiveData<FilterStateModel> = MutableLiveData()
    val filterLiveDate: LiveData<FilterStateModel> get() = filter

    private val filterEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val filterEnabledLiveDate: LiveData<Boolean> get() = filterEnabled

    var viewModelFilter: FilterStateModel?
        get() = null
    set(value) {
        filter.value = value
    }

    override fun onViewCreated() {

    }

    // создаются параметры для генерации фрагментов для показа определенной категории каждого раздела
    //категории приходят с бэка и мапятся для вариаций в количестве = разделы * категории
    fun setViewArgs(topic: String) {
        fragmentsTopic.value = TOPIC.valueOf(topic)

            .also {
                fragmentParamsModel.value = systemRepository.rewizorCategories.map { item ->
                    FragmentParamsModel(
                        item.guid,
                        item.name,
                        it.name
                    )
                }
            }
    }

    fun refreshData() {
        fragmentParamsModel.value = systemRepository.rewizorCategories.map { item ->
            FragmentParamsModel(
                item.guid,
                item.name,
                fragmentsTopic.value!!.name
            )
        }
    }

    fun getParams(categoryId: String) = fragmentParamsModel.value?.first { it.categoryId == categoryId }



}