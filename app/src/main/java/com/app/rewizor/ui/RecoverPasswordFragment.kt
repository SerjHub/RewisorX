package com.app.rewizor.ui

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.FOCUS_DOWN
import com.app.rewizor.AuthorizationActivity
import com.app.rewizor.R
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.exstension.showMessageAlert
import com.app.rewizor.viewmodel.AuthorizationViewModel
import com.app.rewizor.viewmodel.RecoverPasswordViewModel
import kotlinx.android.synthetic.main.fragment_recover_password.*
import kotlinx.android.synthetic.main.view_input_field.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RecoverPasswordFragment : BaseFragment() {
    override val layout = R.layout.fragment_recover_password
    private val viewModel: RecoverPasswordViewModel by inject()
    private val parentViewModel: AuthorizationViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            emailForRecover.inputField.onTextChange {
                onEmailInserted(it)
            }

            recoverButton.setOnClickListener {
                onRecoverClicked()
            }

            invalidInputLiveData.observeViewModel(viewLifecycleOwner) {
                showMessageAlert(it)
            }

            passwordRecoveredLiveData.observeViewModel(viewLifecycleOwner) {
                (activity as AuthorizationActivity).onPasswordRecovered(it)
            }

            recoverFailedInfoLiveData.observeViewModel(viewLifecycleOwner) {
                showMessageAlert(it)
            }

            insertedEmailLiveData.observeViewModel(viewLifecycleOwner) {
                emailForRecover.inputField.setText(it)
            }

            setSharedVM(parentViewModel)
        }


        emailForRecover.inputField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                Handler().postDelayed(
                    { recover_fragment_scroll.fullScroll(FOCUS_DOWN) },
                    150
                )
            }
        }

    }

    override val toolbarTitle: String?
        get() = null

    override val TAG: String
        get() = RecoverPasswordFragment::class.java.name

}