package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.ui.model.FragmentParamsModel

class TopicViewModel(
    private val systemRepository: SystemRepository
) : BaseViewModel() {

    private val fragmentsTopic: MutableLiveData<String> = MutableLiveData()
    val fragmentsTopicLiveData: LiveData<String> get() = fragmentsTopic

    private val fragmentParamsModel: MutableLiveData<List<FragmentParamsModel>> = MutableLiveData()
    val fragmentParamsModelLiveData: LiveData<List<FragmentParamsModel>> get() = fragmentParamsModel

    override fun onViewCreated() {

    }

    // создаются параметры для генерации фрагментов для показа определенной категории каждого раздела
    //категории приходят с бэка и мапятся для вариаций в количестве = разделы * категории
    fun setViewArgs(topic: String) {
        fragmentsTopic.value = topic
            .also {
                fragmentParamsModel.value = systemRepository.rewizorCategories.map { item ->
                    FragmentParamsModel(
                        item.guid,
                        item.name,
                        it
                    )
                }
            }
    }

    fun getParams(categoryId: String) = fragmentParamsModel.value?.first { it.categoryId == categoryId }

}