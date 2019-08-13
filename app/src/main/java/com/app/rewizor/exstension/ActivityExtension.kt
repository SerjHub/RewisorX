package com.app.rewizor.exstension

import androidx.fragment.app.FragmentActivity
import com.app.rewizor.StartActivity
import com.app.rewizor.ui.BaseFragment

fun FragmentActivity.replaceFragment(
    container: Int = if (this is StartActivity) StartActivity.FRAGMENT_CONTAINER
    else throw RuntimeException("Put container Id!!!"),
    fragment: BaseFragment
) {
    supportFragmentManager
        .beginTransaction()
        .replace(container, fragment, fragment.TAG)
        .commit()
}