package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.RewizorCategory
import com.app.rewizor.ui.utils.FilterStateModel

class FilterViewModel : BaseViewModel() {
    lateinit var mainViewModel: MainViewModel

    private val categories: MutableLiveData<List<RewizorCategory>> = MutableLiveData()
    val categoriesLiveData: LiveData<List<RewizorCategory>> get() = categories

    lateinit var filterStateModel: FilterStateModel


    override fun onViewCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setSearch(text: String) {
        filterStateModel.searchText = text
    }

    fun setPlace(text: String) {
        filterStateModel.place = text
    }

    fun setCategory(position: Int) {
        filterStateModel.category = categories.value?.get(position)?.guid ?: ""
    }

    fun setAge(text: String) {
        filterStateModel.age = text
    }

}