package com.app.rewizor.exstension

fun String?.isNullOrNorChars(block: NoCharsAction? = null) =
    (isNullOrBlank() || isNullOrEmpty()).also {
        if (it) block?.invoke(this!!)
    }

fun String.isUnProcessable(block: String.() -> Unit): Boolean {
    return isNullOrBlank() || isNullOrEmpty()

}

typealias NoCharsAction = String.() -> Unit
typealias NoCharsActionBool = String.() -> Boolean


