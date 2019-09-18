package com.app.rewizor.ui

import android.app.DatePickerDialog
import android.os.Bundle
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
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FiltersFragment : BaseFragment() {
    private val filterViewModel: FilterViewModel by inject()
    private val parentViewModel: MainViewModel by sharedViewModel()

    lateinit var filters: FilterStateModel

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
            mainViewModel = parentViewModel
            parentViewModel.filterStateLiveData.observeViewModel(viewLifecycleOwner) {
                filters = it
                setUpFiltersUi(it)
                initInputListeners(it)


                categoriesLiveData.observeViewModel(this@FiltersFragment) { list ->

                    categoryField.openItemsDialogListener(list.map { c -> c.name }) { item ->

                        setCategory(item)

                    }

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
                        override fun onChanged(value: Int) { filterViewModel.setAge("$value") }
                        override fun onCleared() { filterViewModel.setAge("0") }
                    }
                    showDialog(childFragmentManager)
                }
            }

            date.setOnClickListener {
                context?.let {  c ->
                    DateTime().also {
                        DatePickerDialog(
                            c,
                            { p: DatePicker, y: Int, m: Int, d: Int ->              //start date

                                DatePickerDialog(
                                    c,
                                    { pE: DatePicker, yE: Int, mE: Int, dE: Int ->  //end date

                                    },
                                    it.year,
                                    it.monthOfYear,
                                    it.dayOfMonth
                                ).show()
                            },
                            it.year,
                            it.monthOfYear,
                            it.dayOfMonth
                        ).show()
                    }
                }
            }
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
            mostRead?.let {
                mostReadItem.isVisible = true
            }
        }
    }

}