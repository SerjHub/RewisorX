package com.app.rewizor

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.app.rewizor.exstension.replaceFragment
import com.app.rewizor.ui.LoginFragment
import com.app.rewizor.ui.LoginFragment.Companion.LOGIN_RECOVERED_PASSWORD_KEY
import com.app.rewizor.ui.RecoverPasswordFragment
import com.app.rewizor.ui.RegistrationFragment
import com.app.rewizor.ui.utils.AUTHORIZATION
import com.app.rewizor.ui.utils.AUTHORIZATION_INTENT_KEY
import com.app.rewizor.viewmodel.StartViewModel
import kotlinx.android.synthetic.main.activity_authorization.*
import org.koin.android.ext.android.inject

class AuthorizationActivity : AppCompatActivity() {
    private val viewModel: StartViewModel by inject()
    var toolbarTitle: String?
        get() = supportActionBar?.title.toString()
        set(value) {
            supportActionBar?.title = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        setToolbar()
        viewModel.onViewCreated()
        setViewModel()
        intent.getStringExtra(AUTHORIZATION_INTENT_KEY)?.let { onStartViewOpen(it) }
    }

    private fun setToolbar() {
        setSupportActionBar(start_toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        start_toolbar.navigationIcon?.setColorFilter(
            ContextCompat.getColor(this, R.color.superWhite),
            PorterDuff.Mode.SRC_ATOP
            )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setViewModel() {
        viewModel.screen.observe(this, Observer { view -> onStartViewOpen(view) })
        viewModel.openMain.observe(this, Observer { if (it) openMainApp() })
    }

    private fun onStartViewOpen(view: String) {
        when(view) {
            AUTHORIZATION.LOGIN.name -> replaceFragment(fragment = LoginFragment())
            AUTHORIZATION.REGISTRATION.name -> replaceFragment(fragment = RegistrationFragment())
            AUTHORIZATION.RECOVER.name -> replaceFragment(fragment = RecoverPasswordFragment())
        }
    }



    fun openLogin() {
        with(supportFragmentManager) {
            fragments.firstOrNull { it is LoginFragment }
                .also { f ->
                    f?.let {
                        beginTransaction().show(it)
                        return@also }
                    replaceFragment(fragment = LoginFragment())
                }
        }

    }

    private fun openMainApp() {
        startActivity(Intent(this@AuthorizationActivity, MainActivity::class.java))
        finish()
    }

    fun onPasswordRecovered(withEmail: String) {
        replaceFragment(fragment = LoginFragment()
            .apply {
                arguments = Bundle().apply { putString(LOGIN_RECOVERED_PASSWORD_KEY, withEmail) }
            })
    }

    private fun onBackClicked() {

    }

    override fun onBackPressed() {
        with(supportFragmentManager.fragments) {
            when {
                first { it.isVisible } is RegistrationFragment &&
                        intent.getStringExtra(AUTHORIZATION_INTENT_KEY) == AUTHORIZATION.REGISTRATION.name -> {
                    forEach { supportFragmentManager.beginTransaction().remove(it) }
                    super.onBackPressed()
                }
                first { it.isVisible } is LoginFragment -> {
                    forEach { supportFragmentManager.beginTransaction().remove(it) }
                    super.onBackPressed()
                }
                else -> replaceFragment(fragment = LoginFragment())

            }
        }
    }

    private fun checkBackward(fragment: Fragment) {
        if (fragment.arguments?.getBoolean("") == true) {
            replaceFragment(fragment = LoginFragment())
        } else {

        }
    }

    private fun isNavigatedFromMain() =
        intent.getStringArrayExtra(AUTHORIZATION_INTENT_KEY) != null
    
    companion object {
        const val FRAGMENT_CONTAINER = R.id.fragment_container
    }
}
