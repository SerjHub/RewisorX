package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import com.app.rewizor.MainActivity
import com.app.rewizor.R
import com.app.rewizor.data.model.Region
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.ui.adapter.CitiesAdapter
import com.app.rewizor.viewmodel.RegionViewModel
import kotlinx.android.synthetic.main.fragment_cities.*
import org.koin.android.ext.android.inject

class RegionsListFragment : BaseFragment() {
    override val layout = R.layout.fragment_cities
    private val viewModel: RegionViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            citiesListLiveData.observeViewModel(viewLifecycleOwner) {
                setAdapter(it)
            }
            setParentViewModel((activity as MainActivity).viewModel)
            onViewCreated()
        }

        searchCity.onTextChange {
            viewModel.searchInserted(it)
        }
    }

    private fun setAdapter(list: List<Region>) {
        cities_list_view.adapter = CitiesAdapter(list) {
            viewModel.onCityClicked(it)
        }
    }

    private fun setupToolBar() {

    }


    override val TAG: String
        get() = this::class.java.name

}