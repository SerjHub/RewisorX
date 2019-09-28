package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import com.app.rewizor.MainActivity
import com.app.rewizor.R
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.exstension.showMessageAlert
import com.app.rewizor.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.view_input_field.view.*
import org.koin.android.ext.android.inject

class ProfileFragment : BaseFragment() {
    private val profileViewModel: ProfileViewModel by inject()
    private val parentViewModel
        get() = (activity as? MainActivity)?.viewModel

    override val layout: Int
        get() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()

    }

    private fun setObservers() {
        with(profileViewModel) {
            parentViewModel?.let { mainViewModel = it }
            accountLiveData.observeViewModel(viewLifecycleOwner) { acc ->
                with(acc) {
                    firstName?.let { firstNameProfile.inputField.setText(it) }
                    lastName?.let { lastNameProfile.inputField.setText(it) }
                    middleName?.let { middleNameProfile.inputField.setText(it) }
                    email?.let { emailProfile.inputField.setText(it) }
                    phone?.let { phoneProfile.inputField.setText(it) }
                }
            }
            saveErrorLiveData.observeViewModel(viewLifecycleOwner) {
                showMessageAlert("Не удалось сохранить изменения, попробуйте позднее")
            }
        }
    }

    private fun setListeners() {
        with(profileViewModel) {
            firstNameProfile.inputField.onTextChange { onFirstNameInserted(it) }
            middleNameProfile.inputField.onTextChange { onMiddleNameInserted(it) }
            lastNameProfile.inputField.onTextChange { onLastNameInserted(it) }
            phoneProfile.inputField.onTextChange { onPhoneNameInserted(it) }
            emailProfile.inputField.onTextChange { onEmailNameInserted(it) }
            saveProfile.setOnClickListener { onSaveClicked() }
            logoutProfile.setOnClickListener { onLogout() }
        }

    }

    override val TAG: String
        get() = "ProfileFragment"
}