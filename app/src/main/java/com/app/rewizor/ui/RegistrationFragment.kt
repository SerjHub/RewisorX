package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import com.app.rewizor.R
import com.app.rewizor.StartActivity
import com.app.rewizor.viewmodel.RegistrationViewModel
import org.koin.android.ext.android.inject

class RegistrationFragment : BaseFragment() {
    override val layout = R.layout.fragment_registration
    private val viewModel: RegistrationViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        (activity as StartActivity).toolbarTitle = TOOLBAR_TITLE
    }

    private fun initClickListeners() {
        with(viewModel) {
            onRegistrationClicked()
        }
    }

    override val TAG: String
        get() = RegistrationFragment::class.java.name

    companion object {
        const val TOOLBAR_TITLE = "Регистрация"
    }
}