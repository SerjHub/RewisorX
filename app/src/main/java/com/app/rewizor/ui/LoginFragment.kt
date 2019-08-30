package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.app.rewizor.AuthorizationActivity
import com.app.rewizor.R
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.exstension.showMessageAlert
import com.app.rewizor.viewmodel.AuthorizationViewModel
import com.app.rewizor.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.view_input_field.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment() {
    override val layout = R.layout.fragment_login
    private val viewModel: LoginViewModel by inject()

    private val parentViewModel: AuthorizationViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        initListeners()
        getExtras()
    }

    override fun onResume() {
        super.onResume()
        (activity as AuthorizationActivity).toolbarTitle = TOOLBAR_TITLE
    }

    private fun getExtras() {
        arguments?.run {
            loginInputView.inputField.setText(
                getString(LOGIN_RECOVERED_PASSWORD_KEY) ?: ""
            )
        }
    }

    private fun setObservers() {
        with(viewModel) {
            onLoginFailedLiveData.observe(viewLifecycleOwner, Observer { showLoginFailAlert(it) })
            onValidationErrorLiveData.observe(viewLifecycleOwner, Observer { showLoginFailAlert(it.info)})
            validationInfoLiveData.observe(viewLifecycleOwner, Observer { showError(it) })
        }
        viewModel.setSharedViewModel(parentViewModel)
        viewModel.onViewCreated()
    }

    private fun initListeners() {
        with(viewModel) {
            loginButton.setOnClickListener { loginRequest() }
            loginInputView.inputField.onTextChange { onLoginInput(it) }
            passwordInputView.inputField.onTextChange { onPasswordInput(it) }
            recoverPasswordButton.setOnClickListener { onRecoverPasswordClicked() }
            registrationButton.setOnClickListener { onRegistrationClicked() }
        }
    }

    private fun showLoginFailAlert(text: String) {
        showMessageAlert(text)
    }

    private fun showError(list: List<LoginViewModel.VALIDATION>) {
        showMessageAlert(
            StringBuilder().also { builder ->
                list.forEach { builder.append("${it.info}\n") }
            }.toString()
        )
    }

    override val TAG: String
        get() = LoginFragment::class.java.name

    companion object {
        const val TOOLBAR_TITLE = "Авторизация"

        const val LOGIN_RECOVERED_PASSWORD_KEY = "recovered_password_login"

    }
}