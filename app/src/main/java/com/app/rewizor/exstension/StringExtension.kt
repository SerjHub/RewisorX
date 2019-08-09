package com.app.rewizor.exstension

fun String?.isNullOrNorChars() =
    isNullOrBlank() || isNullOrEmpty()