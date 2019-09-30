package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import com.app.rewizor.MainActivity
import com.app.rewizor.R

class AboutFragment : BaseFragment() {
    override val layout: Int
        get() = R.layout.fragment_about

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbarTitle = "О приложении"
    }

    override val TAG: String
        get() = "AboutFragment"

    override val toolbarTitle: String? = TOOLBAR_TITLE

    companion object {
        const val TOOLBAR_TITLE = "О приложении"
    }
}