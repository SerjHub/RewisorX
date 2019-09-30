package com.app.rewizor.ui

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.app.rewizor.MainActivity
import com.app.rewizor.R
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.showMessageAlert
import com.app.rewizor.viewmodel.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject

class SettingsFragment : BaseFragment() {
    override val layout: Int
        get() = R.layout.fragment_settings

    private val settingsViewModel: SettingsViewModel by inject()

    private val mainViewModel
        get() = (activity as? MainActivity)?.viewModel

    private val anonymusAction = CompoundButton.OnCheckedChangeListener { v, checked ->
        showMessageAlert("Войдите или зарегистрируйтесь, чтобы подписаться на уведомления")
        (v as? SwitchMaterial)?.isChecked = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        with(settingsViewModel) {
            accountLiveData.observeViewModel(viewLifecycleOwner) {
                with(it) {
                    news_push.isChecked = newsPushes
                    article_push.isChecked = materialsPushes
                    favorite_push.isChecked = favoritesPushes
                }
            }

            hasAccountLiveData.observeViewModel(viewLifecycleOwner) {
                setAnonymListeners()
            }

            onViewCreated()
        }
    }


    private fun setAnonymListeners() {
        news_push.setOnCheckedChangeListener(anonymusAction)
        article_push.setOnCheckedChangeListener(anonymusAction)
        favorite_push.setOnCheckedChangeListener(anonymusAction)
    }


    private fun setListeners() {
        with(settingsViewModel) {
            news_push.setOnCheckedChangeListener { buttonView, isChecked ->  notificationChecked(news = isChecked) }
            article_push.setOnCheckedChangeListener { buttonView, isChecked ->  notificationChecked(articles = isChecked) }
            favorite_push.setOnCheckedChangeListener { buttonView, isChecked ->  notificationChecked(favorites = isChecked) }
            saveSettings.setOnClickListener { mainViewModel?.openLastTopic() }
            infoSettings.setOnClickListener { mainViewModel?.aboutClicked() }
        }
    }

    override val toolbarTitle: String?
        get() = "Настройки"

    override val TAG: String
        get() = "SettingsFragment"

}