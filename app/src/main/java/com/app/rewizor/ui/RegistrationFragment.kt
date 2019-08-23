package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.app.rewizor.R
import com.app.rewizor.StartActivity
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.exstension.showMessageAlert
import com.app.rewizor.viewmodel.RegistrationViewModel
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.view_input_field.view.*
import org.koin.android.ext.android.inject

class RegistrationFragment : BaseFragment() {
    override val layout = R.layout.fragment_registration
    private val viewModel: RegistrationViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        (activity as StartActivity).toolbarTitle = TOOLBAR_TITLE
    }

    private fun setObservers() {
        with(viewModel) {
            validationInfoLiveData.observe(viewLifecycleOwner, Observer { updateValidationUi(it) })

        }
    }

    private fun initListeners() {
        with(viewModel) {
            registerNewUserButton.setOnClickListener { onRegistrationClicked() }
            lastName.inputField.onTextChange { setLast(it) }
            firstName.inputField.onTextChange { setFirst(it) }
            email.inputField.onTextChange { setEmail(it) }
            phone.inputField.onTextChange { setPhone(it) }
            politics_check.setOnCheckedChangeListener { _, isChecked -> setAgreement(isChecked) }
        }
    }

    private fun updateValidationUi(list: List<RegistrationViewModel.VALIDATION>) {
        lastName.setChecked()
        firstName.setChecked()
        email.setChecked()
        phone.setChecked()
        showMessageAlert(
            StringBuilder().also { builder ->
                list.forEach { builder.append("${it.info}\n") }
            }.toString()
        ) {
            list.forEach {
                when (it) {
                    RegistrationViewModel.VALIDATION.LASTNAME -> lastName.setUnchecked()
                    RegistrationViewModel.VALIDATION.FIRSTNAME -> firstName.setUnchecked()
                    RegistrationViewModel.VALIDATION.EMAIL -> email.setUnchecked()
                    RegistrationViewModel.VALIDATION.PHONE -> phone.setUnchecked()
                    RegistrationViewModel.VALIDATION.AGREEMENT -> {}
                }
            }
        }
    }

    override val TAG: String
        get() = RegistrationFragment::class.java.name

    companion object {
        const val TOOLBAR_TITLE = "Регистрация"
    }
}