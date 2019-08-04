package com.app.rewizor.ui.custom

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.app.rewizor.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_input_field.view.*


class InputField @JvmOverloads constructor(
    context: Context,
    attrRes: AttributeSet? = null,
    styleRes: Int = 0
) : FrameLayout(context,attrRes, styleRes), LayoutContainer {
    override val containerView: View = LayoutInflater.from(context).inflate(R.layout.view_input_field, this, true)
    
    init {
        context.obtainStyledAttributes(attrRes, R.styleable.InputField).apply {
            if(getBoolean(R.styleable.InputField_inputPassword, false))
            inputField.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            inputFieldLayout.hint = getString(R.styleable.InputField_inputFieldHint)
            inputField.setText(getString(R.styleable.InputField_inputFieldText))
            recycle()
        }
    }
}