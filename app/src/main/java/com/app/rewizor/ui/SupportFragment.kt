package com.app.rewizor.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.app.rewizor.R
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.onTextChange
import com.app.rewizor.viewmodel.MainViewModel
import com.app.rewizor.viewmodel.SupportViewModel
import kotlinx.android.synthetic.main.fragment_support.*
import kotlinx.android.synthetic.main.view_input_field.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SupportFragment : BaseFragment() {
    override val layout = R.layout.fragment_support

    private val supportViewModel: SupportViewModel by inject()
    private val mainViwModel: MainViewModel by sharedViewModel()
    override val TAG = "SupportFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel(supportViewModel)

        sendEmail.setOnClickListener {
            val mIntent = Intent(Intent.ACTION_SEND)
            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(SUPPORT_EMAIL))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT)
            mIntent.putExtra(Intent.EXTRA_TEXT, message.inputField.text.toString())
            startActivity(Intent.createChooser(mIntent, "Выберите программу для отправки"))
        }
    }

    private fun setupViewModel(vm: SupportViewModel) {
        with(vm) {
            currentEmailLiveData.observeViewModel(viewLifecycleOwner) {
                it.first?.let { t -> currentEmail.inputField.setText(t) }
                if (it.second) setListener()
            }
            onViewCreated()
        }
    }

    private fun setListener() {
        currentEmail.inputField.onTextChange {
            supportViewModel.currentEmailLiveData
        }

        message.inputField.text
    }

    override val toolbarTitle: String?
        get() = "Поддержка"

    companion object {
        const val SUPPORT_EMAIL = "info@rewizor.ru"
        const val SUBJECT = "Поддержка"

    }

}