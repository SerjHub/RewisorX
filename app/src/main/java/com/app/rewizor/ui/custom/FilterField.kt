package com.app.rewizor.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.app.rewizor.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_filter_input.view.*

class FilterField @JvmOverloads constructor(
    context: Context,
    attrRes: AttributeSet? = null,
    styleRes: Int = 0
) : FrameLayout(context,attrRes, styleRes), LayoutContainer {
    override val containerView: View =
        LayoutInflater.from(context).inflate(R.layout.view_filter_input, this, true)

    init {
        context.obtainStyledAttributes(attrRes, R.styleable.FilterField).apply {
            getString(R.styleable.FilterField_filterTitle)
                .also {
                    if (it.isNullOrEmpty()) currentFilterValue.hint = getString(R.styleable.FilterField_filterHint)
                    else currentFilterValue.setText(it)
                }

            filterIcon.setImageDrawable(
                getDrawable(R.styleable.FilterField_icon)
            )

            recycle()
        }
    }


}