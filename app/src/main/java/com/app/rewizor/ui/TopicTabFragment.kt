package com.app.rewizor.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.app.rewizor.MainActivity
import com.app.rewizor.R
import com.app.rewizor.ui.CategoryListFragment.Companion.CATEGORY_NAME_ARG
import com.app.rewizor.ui.model.FragmentParamsModel
import com.app.rewizor.ui.utils.ListFragmentProvider
import com.app.rewizor.ui.utils.TOPIC_KEY
import com.app.rewizor.viewmodel.MainViewModel
import com.app.rewizor.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.fragment_topic.*
import org.koin.android.ext.android.inject

class TopicTabFragment : TabFragment() {

    override val tabViewId = category_tabs
    override val pagerViewId = category_pager
    override val layout = R.layout.fragment_topic
    override val TAG: String = "TopicTabFragment"
    override val viewModel: TopicViewModel by inject()
    lateinit var topicFragments: List<CategoryListFragment>
    val parentViewModel: MainViewModel
        get() = (activity as MainActivity).viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        category_tabs.setupWithViewPager(category_pager)
        setupViewModel(viewModel)
    }

    private fun setupViewModel(viewModel: TopicViewModel) {
        with(viewModel) {
            fragmentParamsModelLiveData.observe(
                viewLifecycleOwner,
                Observer { createFragments(it) })
            setViewArgs(
                arguments?.getString(TOPIC_KEY)
                    ?: throw NullPointerException("No arguments to show data")
            )
        }
    }

    private fun createFragments(withParams: List<FragmentParamsModel>) {
        topicFragments = ListFragmentProvider.getTopicFragments(withParams)
        setPagerAdapter()
    }

    private fun setPagerAdapter() {
        val adapter = object :
            FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = topicFragments[position]
            override fun getCount() = topicFragments.size
            override fun getPageTitle(position: Int) =
                topicFragments[position].arguments!!.getString(CATEGORY_NAME_ARG)
        }
        category_pager.adapter = adapter
    }

    fun onOpenFilter() {
        Log.i("FindOpen", "opened")
        category_tabs.isVisible = false
        category_pager.isVisible = false
        childFragmentManager
            .beginTransaction()
            .add(topic_root.id, FiltersFragment(), FiltersFragment.TRANSACTION_TAG)
            .commit()
    }

    fun onCloseFilter() {
        Log.i("FindOpen", "close")
        category_tabs.isVisible = true
        category_pager.isVisible = true
        childFragmentManager
            .findFragmentByTag(FiltersFragment.TRANSACTION_TAG)
            ?.also {
                childFragmentManager
                    .beginTransaction()
                    .remove(it)
                    .commit()
            }
        viewModel.onFiltersUpdated()
    }

    companion object {
        fun getInstance(bundle: Bundle? = null) = TopicTabFragment().apply { arguments = bundle }
    }
}