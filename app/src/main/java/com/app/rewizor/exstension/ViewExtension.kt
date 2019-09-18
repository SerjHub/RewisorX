package com.app.rewizor.exstension

import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun View.openItemsDialogListener(
    items: List<CharSequence>,
    listener: (Int) -> Unit
) {
    setOnClickListener {
        MaterialAlertDialogBuilder(context)
            .apply {
                setItems(items.toTypedArray()) { _, which -> listener.invoke(which) }
                setNegativeButton(android.R.string.cancel, null)
                show()
            }
    }
}