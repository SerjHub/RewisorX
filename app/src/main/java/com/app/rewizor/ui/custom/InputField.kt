package com.app.rewizor.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
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
            inputField.inputType = setInputType(this)
            Log.i("FindInp", "${inputField.inputType}")
            inputFieldLayout.hint = getString(R.styleable.InputField_inputFieldHint)
            inputField.setText(getString(R.styleable.InputField_inputFieldText))
            recycle()
        }
    }

    private fun setInputType(typedArray: TypedArray) =
        typedArray.run {
            when {
                getBoolean(R.styleable.InputField_inputPassword, false) ->
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                getBoolean(R.styleable.InputField_inputEmail, false) ->
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT
                else -> InputType.TYPE_CLASS_TEXT
            }
        }

    fun setChecked() {
        imageCheck.setImageResource(R.drawable.ic_check)
    }

    fun setUnchecked() {
        imageCheck.setImageResource(R.drawable.ic_unchecked)
    }

}