package com.app.rewizor.exstension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onTextChange(listener: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           listener(s.toString())
        }
    })
}