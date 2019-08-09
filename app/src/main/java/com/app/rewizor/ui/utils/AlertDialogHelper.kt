package com.app.rewizor.ui.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object AlertDialogHelper {

    fun showSingleActionAlert(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}