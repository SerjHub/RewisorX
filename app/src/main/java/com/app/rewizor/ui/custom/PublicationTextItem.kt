package com.app.rewizor.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.app.rewizor.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_text_info.view.*

class PublicationTextItem @JvmOverloads constructor(
    context: Context,
    attrRes: AttributeSet? = null,
    styleRes: Int = 0
) : FrameLayout(context,attrRes, styleRes), LayoutContainer {
    override val containerView: View = LayoutInflater.from(context).inflate(R.layout.view_text_info, this, true)

    init {
        context.obtainStyledAttributes(attrRes, R.styleable.PublicationTextItem).apply {
            infoTitle.text = getString(R.styleable.PublicationTextItem_title)
            infoBody.text = getString(R.styleable.PublicationTextItem_body)

            if (getBoolean(R.styleable.PublicationTextItem_link, false)) {

               // infoBody.text =
               // infoBody.doOnTextChanged { text, start, count, after ->  }
            }
            recycle()
        }
    }

    fun setContent(text: String) {
        infoBody.text = text
        isVisible = true
    }

}