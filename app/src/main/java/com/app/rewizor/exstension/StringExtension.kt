package com.app.rewizor.exstension

fun String?.isNullOrNorChars(block: NoCharsAction? = null) =
    (isNullOrBlank() || isNullOrEmpty()).also {
        if (it) block?.invoke(String())
    }

fun String.isUnProcessable(block: String.() -> Unit): Boolean {
    return isNullOrBlank() || isNullOrEmpty()

}

fun String?.getNotEmpty() =
    if (isNullOrEmpty()) null else this

typealias NoCharsAction = String.() -> Unit
typealias NoCharsActionBool = String.() -> Boolean


