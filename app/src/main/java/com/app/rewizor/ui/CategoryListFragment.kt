package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import com.app.rewizor.R
import com.app.rewizor.viewmodel.CategoryListViewModel
import org.koin.android.ext.android.inject

class CategoryListFragment : BaseFragment() {
    override val layout = R.layout.fragment_publications_list
    private val viewModel: CategoryListViewModel by inject()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override val TAG = "CategoryListFragment"

    companion object {
        fun getInstance(bundle: Bundle? = null) = CategoryListFragment().apply {
            arguments = bundle
        }
    }

}