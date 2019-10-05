package com.app.rewizor.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.app.rewizor.R
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.exstension.openItemsDialogListener
import com.app.rewizor.ui.utils.*
import com.app.rewizor.viewmodel.FilterViewModel
import com.app.rewizor.viewmodel.MainViewModel
import com.app.rewizor.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.android.synthetic.main.view_filter_input.view.*
import org.joda.time.DateTime
import org.koin.android.ext.android.inject

class FiltersFragment : BaseFragment(), PickerDialog.NumberListener {
    private val filterViewModel: FilterViewModel by inject()
    private val parentViewModel: TopicViewModel
        get() = (parentFragment as TopicTabFragment).viewModel
    private val mainViewModel: MainViewModel?
        get() = (parentFragment as? TopicTabFragment)?.parentViewModel


    override val layout: Int
        get() = R.layout.fragment_filters

    override val TAG: String
        get() = "FiltersFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        with(filterViewModel) {
            this@FiltersFragment.mainViewModel?.let { this.mainViewModel = it }

            parentViewModel.filterStateLiveData.observeViewModel(viewLifecycleOwner) {
                filterStateModel = it
                onViewCreated()
                setUpFiltersUi(it)
                initInputListeners(it)

            }

            resetUiLiveData.observeViewModel(viewLifecycleOwner) {
                setUpFiltersUi(filterStateModel)
            }


            categoriesLiveData.observeViewModel(viewLifecycleOwner) { list ->

                categoryField.openItemsDialogListener(list.map { c -> c.name }) { item ->

                    setCategory(item)

                }

            }

            dateLiveData.observeViewModel(viewLifecycleOwner) {
                date.currentFilterValue.setText(it)
            }

            reChangeEndDateLiveData.observeViewModel(this@FiltersFragment) {
                context?.let { c ->
                    showEndDateDialog(c, it.year, it.monthOfYear, it.dayOfMonth)
                }
            }
        }
    }

    override fun onChanged(value: Int) {
        ages.currentFilterValue.setText(getAgeTitle("$value"))
        filterViewModel.setAge("$value")
    }

    override fun onCleared() {
        ages.currentFilterValue.setText(getAgeTitle("0"))
        filterViewModel.setAge("0")
    }

    private fun initInputListeners(model: FilterStateModel) {
        model.apply {
            events.setEditable()
            events.currentFilterValue.onTextChange {
                filterViewModel.setSearch(it)
            }
            places.setEditable()
            places.currentFilterValue.onTextChange {
                filterViewModel.setPlace(it)
            }
            ages.clickableView.setOnClickListener {


                val picker = NumberPicker(activity)
                    .apply {
                        minValue = 0
                        maxValue = 18
                        wrapSelectorWheel = false
                    }
                picker.setOnValueChangedListener { _, _, newVal ->
                    ages.currentFilterValue.setText(getAgeTitle("$newVal"))
                    filterViewModel.setAge("$newVal")
                }
                AlertDialog.Builder(activity!!).let {
                    it.setTitle("Выберите возраст")
                    it.setNegativeButton("Отменить") { _, _ ->
                        ages.currentFilterValue.setText(getAgeTitle(""))
                        filterViewModel.setAge("0")
                    }
                    it.setPositiveButton("Применить") { _, _ -> }
                    it.setView(picker)
                    it.create()
                        .show()
                }


            }

            popularSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                filterViewModel.setPopular(isChecked)
            }

            recommendedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                filterViewModel.setRecommended(isChecked)
            }

            date.clickableView.setOnClickListener {
                context?.let { c ->
                    DateTime.now().also {
                        DatePickerDialog(
                            c,
                            { p: DatePicker, y: Int, m: Int, d: Int ->
                                filterViewModel.setStartDate(y, m + 1, d)
                                showEndDateDialog(c, y, m, d)
                            },
                            it.year,
                            it.monthOfYear - 1,
                            it.dayOfMonth
                        ).apply {
                            setOnCancelListener { filterViewModel.releaseDateFilter() }
                            show()
                        }
                    }
                }
            }
        }

        saveFilters.setOnClickListener { mainViewModel?.filterClicked() }
        clearFilters.setOnClickListener { filterViewModel.onClear() }
    }

    private fun showEndDateDialog(c: Context, y: Int, m: Int, d: Int) {
        DatePickerDialog(
            c,
            { pE: DatePicker, yE: Int, mE: Int, dE: Int ->
                filterViewModel.setEndDate(yE, mE + 1, dE)
            },
            y,
            m,
            d
        ).apply {
            show()
        }
    }


    private fun setUpFiltersUi(model: FilterStateModel) {
        with(model) {

            searchText?.let {
                events.isVisible = true
                if (it.isNotEmpty()) events.currentFilterValue.setText(it)
                else events.currentFilterValue.hint = when (this) {
                    is Main -> searchTextTitle
                    is Afisha -> searchTextTitle
                    is Materials -> searchTextTitle
                    is News -> searchTextTitle
                    else -> "Поиск"
                }

            }
            dates?.let {
                date.isVisible = true
                date.currentFilterValue.setText(DatePrinter.printIsoPeriod(it))
            }
//            category?.let {
//                categoryField.isVisible = true
//            }
            age?.let {
                ages.isVisible = true
                ages.currentFilterValue.setText(getAgeTitle(it))
            }
            place?.let {
                places.isVisible = true
                places.currentFilterValue.setText(it)
            }
            popular?.let {
                popularItem.isVisible = true
                popularSwitch.isChecked = it
            }
            recommend?.let {
                recommendedItem.isVisible = true
                recommendedSwitch.isChecked = it
            }
            mostRead?.let {
                mostReadItem.isVisible = true
            }
        }
    }

    private fun getAgeTitle(a: String) = if (a.isNotEmpty()) "Возраст $a+" else "Возраст"

    override val toolbarTitle: String? = null
    companion object {
        const val TRANSACTION_TAG = "filtersFragment"
    }

}