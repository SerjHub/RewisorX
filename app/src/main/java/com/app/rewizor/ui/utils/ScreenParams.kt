package com.app.rewizor.ui.utils

const val CATEGORY_KEY = "rewizorCategories"
const val TOPIC_KEY = "topic"

const val AUTHORIZATION_INTENT_KEY = "auth_screen"

enum class TOPIC(val title: String,val requestKey: String?, val filters: FilterStateModel? = null){
    MAIN("Главная", null, Main()),
    NEWS("Новости", "news", News()),
    AFISHA("Афиша", "afisha", Afisha()),
    MATERIALS("Материалы", "articles", Materials()),
    PLACES("Места", "places", Places()),
    KIDS("Для детей", "mail")
}





enum class AUTHORIZATION {
    LOGIN,
    REGISTRATION,
    RECOVER
}