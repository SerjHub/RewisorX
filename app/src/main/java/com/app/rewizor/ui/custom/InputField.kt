package com.app.rewizor.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.app.rewizor.R
import com.google.android.material.textfield.TextInputLayout
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
            setInputType(this)
            inputFieldLayout.hint = getString(R.styleable.InputField_inputFieldHint)
            inputField.setText(getString(R.styleable.InputField_inputFieldText))
            recycle()
        }
    }

    private fun setInputType(typedArray: TypedArray) =
        typedArray.run {
            when {
                getBoolean(R.styleable.InputField_inputPassword, false) -> {
                    inputField.transformationMethod = PasswordTransformationMethod()
                    inputFieldLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }

//                getBoolean(R.styleable.InputField_inputEmail, false) -> {
//                    inputField.transformationMethod = TransformationMethod
//                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT
//                }

                else -> InputType.TYPE_CLASS_TEXT
            }
        }

    fun setChecked() {
        imageCheck.setImageResource(R.drawable.ic_check)
        imageCheck.isVisible = true
        underlineView.isVisible = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            underlineView.setBackgroundColor(resources.getColor(R.color.edit_text_line, null))
        }
    }

    fun setUnchecked() {
        imageCheck.setImageResource(R.drawable.ic_unchecked)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            underlineView.setBackgroundColor(resources.getColor(R.color.brightRed, null))
        }
    }

}