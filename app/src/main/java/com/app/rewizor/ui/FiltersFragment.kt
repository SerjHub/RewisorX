package com.app.rewizor.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.core.view.isVisible
import com.app.rewizor.R
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.exstension.openItemsDialogListener
import com.app.rewizor.ui.utils.FilterStateModel
import com.app.rewizor.viewmodel.FilterViewModel
import com.app.rewizor.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.android.synthetic.main.view_filter_input.view.*
import org.joda.time.DateTime
import org.koin.android.ext.android.inject

class FiltersFragment : BaseFragment() {
    private val filterViewModel: FilterViewModel by inject()
    private val parentViewModel: MainViewModel
        get() = (parentFragment as TopicTabFragment).parentViewModel

    lateinit var filters: FilterStateModel

    override val layout: Int
        get() = R.layout.fragment_filters

    override val TAG: String
        get() = "FiltersFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("FindFragc", "crewted")
        setObservers()
    }

    private fun setObservers() {
        Log.i("FindOpenFilters", "st observe ${parentViewModel.filterStateLiveData.value}")
        with(filterViewModel) {
            mainViewModel = parentViewModel
            parentViewModel.filterStateLiveData.observeViewModel(viewLifecycleOwner) {
                Log.i("FindOpenFilters", "onChange $it")
                filters = it
                setUpFiltersUi(it)
                initInputListeners(it)

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

    private fun initInputListeners(model: FilterStateModel) {
        model.apply {
            events.currentFilterValue.onTextChange {
                filterViewModel.setSearch(it)
            }
            places.currentFilterValue.onTextChange {
                filterViewModel.setPlace(it)
            }
            ages.setOnClickListener {
                with(PickerDialog()) {
                    listener = object : PickerDialog.NumberListener {
                        override fun onChanged(value: Int) {
                            filterViewModel.setAge("$value")
                        }

                        override fun onCleared() {
                            filterViewModel.setAge("0")
                        }
                    }
                    showDialog(childFragmentManager)
                }
            }

            popularSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                filterViewModel.setPopular(isChecked)
            }

            recommendedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                filterViewModel.setRecommended(isChecked)
            }


            date.setOnClickListener {
                context?.let { c ->
                    DateTime().also {
                        DatePickerDialog(
                            c,
                            { p: DatePicker, y: Int, m: Int, d: Int ->
                                filterViewModel.setStartDate(y, m, d)
                                showEndDateDialog(c, y, m, d)
                            },
                            it.year,
                            it.monthOfYear,
                            it.dayOfMonth
                        ).apply {
                            setOnCancelListener { filterViewModel.releaseDateFilter() }
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun showEndDateDialog(c: Context, y: Int, m: Int, d: Int) {
        DatePickerDialog(
            c,
            { pE: DatePicker, yE: Int, mE: Int, dE: Int ->
                filterViewModel.setEndDate(yE, mE, dE)
            },
            y,
            m,
            d
        ).apply {
            setOnCancelListener { filterViewModel.releaseEndDateFilter() }
            show()
        }
    }


    private fun setUpFiltersUi(model: FilterStateModel) {
        with(model) {
            searchText?.let {
                events.isVisible = true
            }
            dates?.let {
                date.isVisible = true
            }
            category?.let {
                categoryField.isVisible = true
            }
            age?.let {
                ages.isVisible = true
            }
            place?.let {
                places.isVisible = true
            }
            popular?.let {
                popularItem.isVisible = true
            }
            recommend?.let {
                recommendedItem.isVisible = true
            }
//            mostRead?.let {
//                mostReadItem.isVisible = true
//            }
        }
    }

    companion object {
        const val TRANSACTION_TAG = "filtersFragment"
    }

}