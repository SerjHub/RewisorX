package com.app.rewizor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.app.rewizor.exstension.replaceFragment
import com.app.rewizor.ui.LoginFragment
import com.app.rewizor.viewmodel.StartViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.getViewModel

class StartActivity : AppCompatActivity() {
    private val viewModel: StartViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        viewModel.onViewCreated()
        setViewModel()
    }

    private fun setViewModel() {
        viewModel.screen.observe(this, Observer { view ->  onStartViewOpen(view) })
        viewModel.openMain.observe(this, Observer { if (it) openMainApp() })
    }

    private fun onStartViewOpen(view: StartViewModel.StartView) {
        when(view) {
            StartViewModel.StartView.LOGIN -> replaceFragment(fragment = LoginFragment())
            StartViewModel.StartView.REGISTRATION -> replaceFragment(fragment = LoginFragment())
            StartViewModel.StartView.RECOVER -> replaceFragment(fragment = LoginFragment())
        }
    }

    private fun openMainApp() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
    
    companion object {
        const val FRAGMENT_CONTAINER = R.id.fragment_container
    }
}
