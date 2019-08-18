package com.app.rewizor.ui.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog


class Alerts {
    companion object {
        fun showAlertToUser(context: Context, message: String, action: AlertMessageAction? = null) {
            AlertDialog.Builder(context)
                .setMessage(message)
                .setOnDismissListener { action?.invoke() }
                .show()
        }
    }
}
typealias AlertMessageAction = () -> Unit