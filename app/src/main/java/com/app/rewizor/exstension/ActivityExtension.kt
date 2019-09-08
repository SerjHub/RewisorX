package com.app.rewizor.exstension

import androidx.fragment.app.FragmentActivity
import com.app.rewizor.AuthorizationActivity
import com.app.rewizor.MainActivity
import com.app.rewizor.ui.BaseFragment

fun FragmentActivity.replaceFragment(
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

fun FragmentActivity.removeFragment(fragment: BaseFragment) {
    supportFragmentManager
        .beginTransaction()
        .remove(fragment)
        .commit()
}