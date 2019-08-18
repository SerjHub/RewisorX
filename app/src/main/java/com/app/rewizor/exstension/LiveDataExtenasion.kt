package com.app.rewizor.exstension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any>LiveData<T>.observeViewModel(owner: LifecycleOwner, observeBlock: (T) -> Unit) {
    observe(owner, Observer { observeBlock.invoke(it) })
}