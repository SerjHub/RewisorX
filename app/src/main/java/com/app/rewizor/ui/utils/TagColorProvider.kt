package com.app.rewizor.ui.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.app.rewizor.R

object TagColorProvider {
    fun setColorToView(view: View, category: String) =
        when (category.toLowerCase()) {
            "театр" -> R.color.theaters
            "литература" -> R.color.literature
            "кино" -> R.color.cinema
            "музыка" -> R.color.music
            "музеи" -> R.color.museums
            else -> R.color.literature
        }
            .also {
                view.setBackgroundColor(ContextCompat.getColor(view.context, it))
            }

}