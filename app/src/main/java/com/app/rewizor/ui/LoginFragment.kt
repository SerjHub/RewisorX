package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.app.rewizor.R
import com.app.rewizor.viewmodel.LoginViewModel
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment() {
    override val layout = R.layout.fragment_login
    private val viewModel: LoginViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        viewModel.onViewCreated()
    }

    private fun setObservers() {
        viewModel.currentEnteredLogin.observe(viewLifecycleOwner, Observer {  })

    }


    override val TAG: String
        get() = LoginFragment::class.java.name
}