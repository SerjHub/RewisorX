package com.app.rewizor.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.core.view.isVisible
import com.app.rewizor.R
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.exstension.openItemsDialogListener
import com.app.rewizor.ui.utils.DatePrinter
import com.app.rewizor.ui.utils.FilterStateModel
import com.app.rewizor.viewmodel.FilterViewModel
import com.app.rewizor.viewmodel.MainViewModel
import com.app.rewizor.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.android.synthetic.main.view_filter_input.view.*
import org.joda.time.DateTime
import org.koin.android.ext.android.inject

class FiltersFragment : BaseFragment() {
    private val filterViewModel: FilterViewModel by inject()
    private val parentViewModel: TopicViewModel
        get() = (parentFragment as TopicTabFragment).viewModel
    private val mainViewModel: MainViewModel
        get() = (parentFragment as TopicTabFragment).parentViewModel


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
            this.mainViewModel = this@FiltersFragment.mainViewModel

            parentViewModel.filterStateLiveData.observeViewModel(viewLifecycleOwner) {
                filterStateModel = it
                onViewCreated()
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
            events.setEditable()
            events.currentFilterValue.onTextChange {
                filterViewModel.setSearch(it)
            }
            places.setEditable()
            places.currentFilterValue.onTextChange {
                filterViewModel.setPlace(it)
            }
            ages.clickableView.setOnClickListener {

                with(PickerDialog()) {
                    listener = object : PickerDialog.NumberListener {
                        override fun onChanged(value: Int) {
                            ages.currentFilterValue.setText(getAgeTitle("$value"))
                            filterViewModel.setAge("$value")
                        }

                        override fun onCleared() {
                            ages.currentFilterValue.setText(getAgeTitle("0"))
                            filterViewModel.setAge("0")
                        }
                    }
                    showDialog(fragmentManager!!)
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
            show()
        }
    }


    private fun setUpFiltersUi(model: FilterStateModel) {
        with(model) {
            searchText?.let {
                events.isVisible = true
                events.currentFilterValue.setText(it)
            }
            dates?.let {
                date.isVisible = true
                DatePrinter.printIsoPeriod(it)
            }
            category?.let {
                categoryField.isVisible = true
            }
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
//            mostRead?.let {
//                mostReadItem.isVisible = true
//            }
        }
    }

    private fun getAgeTitle(a: String) = if (a.isNotEmpty())"Возраст $a+" else "Возраст"

    companion object {
        const val TRANSACTION_TAG = "filtersFragment"
    }

}