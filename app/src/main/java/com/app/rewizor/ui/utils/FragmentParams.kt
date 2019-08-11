package com.app.rewizor.ui.utils

enum class TOPIC(title: String, requestKey: String){
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