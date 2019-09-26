package com.app.rewizor.ui.utils

const val CATEGORY_KEY = "rewizorCategories"
const val TOPIC_KEY = "topic"

const val AUTHORIZATION_INTENT_KEY = "auth_screen"

enum class TOPIC(val title: String,val requestKey: String?, val filters: FilterStateModel? = null){
    MAIN("Главная", null, Main()),
    NEWS("Новости", "news", News()),
    AFISHA("Афиша", "afisha", Afisha()),
    MATERIALS("Материалы", "articles", Materials()),
    PLACES("МЕСТА", "places", Places()),
    KIDS("ДЛЯ ДЕТЕЙ", "mail")
}





enum class AUTHORIZATION {
    LOGIN,
    REGISTRATION,
    RECOVER
}