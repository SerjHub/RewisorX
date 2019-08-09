package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.app.rewizor.R
import com.app.rewizor.StartActivity
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.ui.utils.AlertDialogHelper
import com.app.rewizor.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.view_input_field.view.*
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment() {
    override val layout = R.layout.fragment_login
    private val viewModel: LoginViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        viewModel.onViewCreated()
        initClickListeners()
    }

    override fun onResume() {
        super.onResume()
        (activity as StartActivity).toolbarTitle = TOOLBAR_TITLE
    }

    private fun setObservers() {
        with(viewModel) {
            onLoginFailedLiveData.observe(viewLifecycleOwner, Observer { showLoginFailAlert(it) })
            onValidationErrorLiveData.observe(viewLifecycleOwner, Observer { showLoginFailAlert(it.info)})
        }
    }

    private fun initClickListeners() {
        with(viewModel) {
            loginButton.setOnClickListener { loginRequest() }
            loginInputView.inputField.onTextChange { onLoginInput(it) }
            passwordInputView.inputField.onTextChange { onPasswordInput(it) }
        }
    }

    private fun showLoginFailAlert(text: String) {
        activity?.let {
            AlertDialogHelper.showSingleActionAlert(it, text)
        }
    }


    override val TAG: String
        get() = LoginFragment::class.java.name

    companion object {
        const val TOOLBAR_TITLE = "Авторизация"
    }
}