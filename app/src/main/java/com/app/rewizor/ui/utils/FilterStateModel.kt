package com.app.rewizor.ui.utils

sealed class FilterStateModel(
    var searchText: String? = null,
    var dates: String? = null,
    var category: String? = null,
    var age: String? = null,
    var place: String? = null,
    var popular: Boolean? = null,
    var recommend: Boolean? = null,
    var mostRead: Boolean? = null,
    var dateToRemote: String? = dates
) {
    abstract fun clear()
    abstract fun isCleared(): Boolean
}

data class Main(
    val searchTextTitle: String = "Поиск"
) : FilterStateModel(
    searchText = ""
) {
    override fun clear() {
        searchText = ""
    }

    override fun isCleared() = searchText!!.isEmpty()
}

data class Places(
    val searchTextTitle: String = "Поиск местам"
) : FilterStateModel(
    searchText = ""
) {
    override fun clear() {
        searchText = ""
    }

    override fun isCleared() = searchText!!.isEmpty()
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
        searchText!!.isEmpty() &&
                dates!!.isEmpty() &&
                category!!.isEmpty() &&
                age!!.isEmpty() &&
                !popular!! &&
                !recommend!!

}

data class News(
    val searchTextTitle: String = "Поиск по новостям"
): FilterStateModel (
    searchText = "",
    age = "",
    dates = "",
    place = ""
) {
    override fun clear() {
        searchText = ""
        age = ""
        dates = ""
        place = ""
    }

    override fun isCleared(): Boolean =
        searchText!!.isEmpty() &&
                age!!.isEmpty() &&
                dates!!.isEmpty() &&
                place!!.isEmpty()

}

data class Materials(
    val searchTextTitle: String = "Поиск материалов"
): FilterStateModel (
    searchText = "",
    age = "",
    dates = "",
    place = "",
    popular = false,
    category = ""
) {
    override fun clear() {
        searchText = ""
        age = ""
        dates = ""
        place = ""
        category = ""
        popular = false

    }

    override fun isCleared(): Boolean =
        searchText!!.isEmpty() &&
                age!!.isEmpty() &&
                dates!!.isEmpty() &&
                place!!.isEmpty() &&
                !mostRead!! &&
                category!!.isEmpty()

}

