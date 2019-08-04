package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import com.app.rewizor.R
import com.app.rewizor.viewmodel.LoginViewModel
import org.koin.android.viewmodel.ext.android.getViewModel

class LoginFragment : BaseFragment() {
    override val layout = R.layout.fragment_login
    val viewModel = getViewModel(LoginViewModel::class)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated()
    }


    override val TAG: String
        get() = LoginFragment::class.java.name
}