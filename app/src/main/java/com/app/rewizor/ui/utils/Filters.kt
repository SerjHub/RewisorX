package com.app.rewizor.ui.utils

sealed class Filters(
    val searchTextTitle: String?,
    val dates: Boolean,
    val category: Boolean,
    val age: Boolean,
    val place: Boolean,
    val popular: Boolean,
    val recommend: Boolean,
    val mostRead: Boolean
)

object Afisha : Filters(
    "Поиск по событиям",
    true,
    true,
    true,
    true,
    true,
    true,
    false
)

object News: Filters (
    "Поиск по новостям",
    true,
    false,
    true,
    true,
    false,
    false,
    false
)

object Materials: Filters (
    "Поиск материалов",
    true,
    true,
    true,
    true,
    false,
    false,
    true
)

