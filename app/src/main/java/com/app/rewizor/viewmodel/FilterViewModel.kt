package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.RewizorCategory
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.ui.utils.DatePrinter
import com.app.rewizor.ui.utils.FilterStateModel
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent
import org.joda.time.DateTime

class FilterViewModel(
    private val systemRepository: SystemRepository
) : BaseViewModel() {
    lateinit var mainViewModel: MainViewModel

    private val categories: MutableLiveData<List<RewizorCategory>> = MutableLiveData()
    val categoriesLiveData: LiveData<List<RewizorCategory>> get() = categories

    private val startDay: MutableLiveData<DateTime> = MutableLiveData()

    private val date: MutableLiveData<String> = MutableLiveData()
    val dateLiveData: LiveData<String> get() = date

    private val reChangeEndDate: MutableLiveData<DateTime> = SingleLiveEvent()
    val reChangeEndDateLiveData: LiveData<DateTime> get() = reChangeEndDate


    lateinit var filterStateModel: FilterStateModel

    override fun onViewCreated() {
        filterStateModel.category?.let { categories.value = systemRepository.rewizorCategories  }
    }

    fun setSearch(text: String) {
        filterStateModel.searchText = text
        mainViewModel.filterEnabled(!filterStateModel.isCleared())
    }

    fun setPlace(text: String) {
        filterStateModel.place = text
        mainViewModel.filterEnabled(!filterStateModel.isCleared())
    }

    fun setCategory(position: Int) {
        filterStateModel.category = categories.value?.get(position)?.guid ?: ""
        mainViewModel.filterEnabled(!filterStateModel.isCleared())
    }

    fun setAge(text: String) {
        filterStateModel.age = text
        mainViewModel.filterEnabled(!filterStateModel.isCleared())
    }

    fun setPopular(enabled: Boolean) {
        filterStateModel.popular = enabled
        mainViewModel.filterEnabled(!filterStateModel.isCleared())
    }

    fun setRecommended(enabled: Boolean) {
        filterStateModel.recommend = enabled
        mainViewModel.filterEnabled(!filterStateModel.isCleared())
    }

    fun setStartDate(y: Int, m: Int, d: Int) {
        startDay.value = DateTime(y, m, d, 0, 0)
        filterStateModel.dates =
            "${DatePrinter.dateToIso(startDay.value!!)}-${DatePrinter.dateToIso(DateTime(y, m, d, 23, 59))}"
        showSelectedDate()
        mainViewModel.filterEnabled(!filterStateModel.isCleared())
    }

    private fun showSelectedDate() {
        date.value = DatePrinter.dateNoYear(DatePrinter.dateToIso(startDay.value!!))
            .let { checkForYear.invoke(it) }
    }

    private val checkForYear: (String) -> String = {
        if (it.contains("$CURRENT_YEAR")) it.removeSuffix("$CURRENT_YEAR") else it
    }

    fun releaseDateFilter() {
        filterStateModel.dates = ""
        date.value = ""
        mainViewModel.filterEnabled(!filterStateModel.isCleared())
    }

    fun setEndDate(y: Int, m: Int, d: Int) {
        DateTime(y,m,d,23,59)
            .also {
                if (it.isAfter(startDay.value)) {
                    filterStateModel.dates = "${DatePrinter.dateToIso(startDay.value!!)}-${DatePrinter.dateToIso(it)}"
                    date.value = DatePrinter.printIsoPeriod(filterStateModel.dates!!)
                    mainViewModel.filterEnabled(!filterStateModel.isCleared())
                }
                else { reChangeEndDate.value = it }
            }

    }

    companion object {
        val CURRENT_YEAR = DateTime.now().year
    }

}