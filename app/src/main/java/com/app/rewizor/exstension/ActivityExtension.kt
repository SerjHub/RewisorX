package com.app.rewizor.exstension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.rewizor.AuthorizationActivity
import com.app.rewizor.MainActivity
import com.app.rewizor.ui.BaseFragment

fun AppCompatActivity.replaceFragment(
    container: Int = when (this) {
        is AuthorizationActivity -> AuthorizationActivity.FRAGMENT_CONTAINER
        is MainActivity -> MainActivity.FRAGMENT_CONTAINER
        else -> throw RuntimeException("Put container Id!!!")
    },
    fragment: BaseFragment
) {
    supportFragmentManager
        .beginTransaction()
        .replace(container, fragment, fragment.TAG)
        .commit()
}

fun AppCompatActivity.removeFragment(fragment: BaseFragment) {
    supportFragmentManager
        .beginTransaction()
        .remove(fragment)
        .commit()
}

fun <T: Fragment>AppCompatActivity.getCurrentFragment(type: Class<T>) =
    supportFragmentManager
        .fragments
        .firstOrNull { it::class.java == type } as? T?
