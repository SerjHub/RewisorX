package com.app.rewizor.ui

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

abstract class TabFragment : BaseFragment() {
    abstract val tabViewId: TabLayout
    abstract val pagerViewId: ViewPager
}