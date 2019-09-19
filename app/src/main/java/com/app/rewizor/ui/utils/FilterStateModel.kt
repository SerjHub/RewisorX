package com.app.rewizor.ui.utils

sealed class FilterStateModel(
    var searchText: String? = null,
    var dates: String? = null,
    var category: String? = null,
    var age: String? = null,
    var place: String? = null,
    var popular: Boolean? = null,
    var recommend: Boolean? = null,
    var mostRead: Boolean? = null
) {
    abstract fun clear()
    abstract fun isCleared(): Boolean
}

object Main : FilterStateModel(
    null, null, null, null, null, null, null, null
) {
    override fun clear() = Unit
    override fun isCleared() = true
}

data class Afisha(
    val searchTextTitle: String = "Поиск по событиям"
) : FilterStateModel(
    searchText = "",
    dates = "",
    category = "",
    age = "",
    place = "",
    popular = false,
    recommend = false
) {
    override fun clear() {
        searchText = ""
        dates = ""
        category = ""
        age = ""
        place = ""
        popular = false
        recommend = false
    }

    override fun isCleared() =
        searchText!!.isNotBlank() &&
                dates!!.isNotBlank() &&
                category!!.isNotBlank() &&
                age!!.isNotBlank() &&
                popular!! &&
                recommend!!

}

//object News: FilterStateModel (
//    "Поиск по новостям",
//    true,
//    false,
//    true,
//    true,
//    false,
//    false,
//    false
//)
//
//object Materials: FilterStateModel (
//    "Поиск материалов",
//    true,
//    true,
//    true,
//    true,
//    false,
//    false,
//    true
//)

