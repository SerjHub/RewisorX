package com.app.rewizor.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.ui.model.FragmentParamsModel
import com.app.rewizor.ui.utils.*

class TopicViewModel(
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    lateinit var mainViewModel: MainViewModel

    private val fragmentsTopic: MutableLiveData<TOPIC> = MutableLiveData()
    val fragmentsTopicLiveData: LiveData<TOPIC> get() = fragmentsTopic

    private val fragmentParamsModel: MutableLiveData<List<FragmentParamsModel>> = MutableLiveData()
    val fragmentParamsModelLiveData: LiveData<List<FragmentParamsModel>> get() = fragmentParamsModel

    private val updateCategory: MutableLiveData<Boolean> = MutableLiveData()
    val updateCategoryLiveData: LiveData<Boolean> get() = updateCategory

    private val filterState: MutableLiveData<FilterStateModel> = MutableLiveData()
    val filterStateLiveData: LiveData<FilterStateModel> get() = filterState

    private val lastFilter: MutableLiveData<FilterStateModel> = MutableLiveData()

    override fun onViewCreated() {
        updateLastFilter()
    }

    fun updateLastFilter() {
        fragmentsTopic.value?.filters?.also {
            lastFilter.value = when (it) {
                is Afisha-> it.copy()
                is News -> it.copy()
                is Materials -> it.copy()
                is Main -> it.copy()
                is Places -> it.copy()
            }
        }
    }

    // создаются параметры для генерации фрагментов для показа определенной категории каждого раздела
    //категории приходят с бэка и мапятся для вариаций в количестве = разделы * категории
    fun setViewArgs(topic: String) {
        fragmentsTopic.value = TOPIC.valueOf(topic)
            .also {
                filterState.value = it.filters
                Log.i("FindFilter", "set state ${filterState.value}")
                fragmentParamsModel.value = systemRepository.rewizorCategories.map { item ->
                    FragmentParamsModel(
                        item.guid,
                        item.name,
                        it.name
                    )
                }
            }
    }

    fun onFiltersUpdated() {
        if (lastFilter == fragmentsTopic.value!!.filters) {
            return
        } else {
            updateLastFilter()
            updateCategory.value = true
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

    fun searchInserted(text: String?) {
        when {
            (text.isNullOrEmpty() || text.length < 3) && !filterState.value?.searchText.isNullOrEmpty() -> {
                filterState.value?.searchText = ""
                onFiltersUpdated()
            }
            (text?.length ?: 0 > 2)  -> {
                filterState.value?.let { it.searchText = text }
                onFiltersUpdated()
            }
        }
    }

    fun getParams(categoryId: String) = fragmentParamsModel.value?.first { it.categoryId == categoryId }
}