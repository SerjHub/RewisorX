package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.app.rewizor.R
import com.app.rewizor.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.topic_fragment.*
import org.koin.android.ext.android.inject

class TopicTabFragment: TabFragment() {

    override val tabViewId = category_tabs
    override val pagerViewId = category_pager
    override val layout = R.layout.topic_fragment
    override val TAG: String = "TopicTabFragment"
    private val viewModel: TopicViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setPagerAdapter() {
        val adapter = object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {

            }

            override fun getCount(): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }

}