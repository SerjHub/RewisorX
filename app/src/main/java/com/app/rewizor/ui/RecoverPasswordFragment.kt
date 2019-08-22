package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import com.app.rewizor.R
import com.app.rewizor.StartActivity
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.exstension.showMessageAlert
import com.app.rewizor.viewmodel.RecoverPasswordViewModel
import kotlinx.android.synthetic.main.fragment_recover_password.*
import kotlinx.android.synthetic.main.view_input_field.view.*
import org.koin.android.ext.android.inject

class RecoverPasswordFragment : BaseFragment() {
    override val layout = R.layout.fragment_recover_password
    private val viewModel: RecoverPasswordViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            emailForRecover.inputField.onTextChange {
                onEmailInserted(it)
            }

            recoverButton.setOnClickListener {
                onRecoverClicked()
            }

            invalidInputLiveData.observeViewModel(viewLifecycleOwner, {
                showMessageAlert(it)
            })

            passwordRecoveredLiveData.observeViewModel(viewLifecycleOwner) {
                (activity as StartActivity).onPasswordRecovered(it)
            }
        }


    }

    override val TAG: String
        get() = RecoverPasswordFragment::class.java.name

}