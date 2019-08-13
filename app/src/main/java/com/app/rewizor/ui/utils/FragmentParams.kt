package com.app.rewizor.ui.utils

const val CATEGORY_KEY = "category"
const val TOPIC_KEY = "topic"

enum class TOPIC(val title: String,val requestKey: String){
    MAIN("ГЛАВНАЯ", "mail"),
    NEWS("НОВОСТИ", "news"),
    AFISHA("АФИША", "mail"),
    MATERIALS("МАТЕРИЛЫ", "mail"),
    PLACES("МЕСТА", "mail"),
    KIDS("ДЛЯ ДЕТЕЙ", "mail")
}
enum class CATEGORY(val categoryTitle: String,val requestKey: String) {
    ALL("ВСЕ", "all"),
    THEATRE("ТЕАТР", "the"),
    MOVIE("КИНО", "movie"),
    LITERATURE("ЛИТЕРАТУРА", "lit")
}

sealed class FragmentParams

object Main : FragmentParams() {

}