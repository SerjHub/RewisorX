package com.app.rewizor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.app.rewizor.exstension.replaceFragment
import com.app.rewizor.ui.LoginFragment
import com.app.rewizor.ui.RecoverPasswordFragment
import com.app.rewizor.ui.RegistrationFragment
import com.app.rewizor.viewmodel.StartViewModel
import kotlinx.android.synthetic.main.activity_start.*
import org.koin.android.ext.android.inject

class StartActivity : AppCompatActivity() {
    private val viewModel: StartViewModel by inject()
    var toolbarTitle: String
        get() = start_toolbar.title.toString()
        set(value) {
            supportActionBar?.title = value
  //          start_toolbar.title = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        setToolbar()
        viewModel.onViewCreated()
        setViewModel()
    }

    private fun setToolbar() {
        setSupportActionBar(start_toolbar as Toolbar)
        supportActionBar?.setHomeButtonEnabled(false)
        start_toolbar.setNavigationOnClickListener { onBackClicked() }

    }

    private fun setViewModel() {
        viewModel.screen.observe(this, Observer { view -> onStartViewOpen(view) })
        viewModel.openMain.observe(this, Observer { if (it) openMainApp() })
    }

    private fun onStartViewOpen(view: StartViewModel.FRAGMENT) {
        supportActionBar?.setHomeButtonEnabled(view != StartViewModel.FRAGMENT.LOGIN)
        when(view) {
            StartViewModel.FRAGMENT.LOGIN -> replaceFragment(fragment = LoginFragment())
            StartViewModel.FRAGMENT.REGISTRATION -> replaceFragment(fragment = RegistrationFragment())
            StartViewModel.FRAGMENT.RECOVER -> replaceFragment(fragment = RecoverPasswordFragment())
        }
    }

    private fun openMainApp() {
        Intent(this, MainActivity::class.java).also { startActivity(it) }
        finish()
    }

    private fun onBackClicked() {
        with(supportFragmentManager) {
            fragments.any { it is LoginFragment }
                .also {
                    if (it) finish()
                    else replaceFragment(fragment = LoginFragment())
                }
            }
    }

    override fun onBackPressed() {
        onBackClicked()
    }
    
    companion object {
        const val FRAGMENT_CONTAINER = R.id.fragment_container
    }
}
