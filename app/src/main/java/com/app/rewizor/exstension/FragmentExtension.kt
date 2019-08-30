package com.app.rewizor.exstension

import androidx.fragment.app.Fragment
import com.app.rewizor.ui.utils.AlertMessageAction
import com.app.rewizor.ui.utils.Alerts

fun Fragment.showMessageAlert(message: String, disMiss: AlertMessageAction? = null) {
    activity?.let {
        Alerts.showAlertToUser(it, message, disMiss)
        return
    }
    onDestroy()
}
