package com.app.rewizor.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.app.rewizor.data.model.NotificationsModel
import com.app.rewizor.data.repositoryImpl.AccountRepositoryImpl

class PreferencesCache(private val context: Application) {

    companion object {
        const val APP_PREFS = "app"
        const val USER_PREFS = "user"
        const val LOGIN = "login_pref"
        const val ACCESS_TOKEN = "token"
        const val SAVED_REGION = "region_id"


        const val NEWS = "news_notif"
        const val ARTICLES = "articles_notif"
        const val FAVORITE = "favorite_notif"

    }

    var sessionToken: String?
        get() = prefs(APP_PREFS)
            .getString(ACCESS_TOKEN, null)
        set(value) {
            prefs(APP_PREFS)
                .edit()
                .putString(ACCESS_TOKEN, value)
                .apply()
            tokenClients.forEach { it.onTokenChanged(value ?: AccountRepositoryImpl.ANON_TOKEN) }
        }

    var savedLogin: String?
        get() = prefs(APP_PREFS)
            .getString(LOGIN, "")
        set(value) {
            prefs(APP_PREFS)
                .edit()
                .putString(LOGIN, value)
                .apply()
        }

    var savedRegionId: Int
        get() = prefs(APP_PREFS)
            .getInt(SAVED_REGION, 0)
        set(value) {
            prefs(APP_PREFS)
                .edit()
                .putInt(SAVED_REGION, value)
                .apply()
            regionClients.forEach { it.onRegionChanged(value) }
        }

    var notificationsModel: NotificationsModel
        get() =
            prefs(USER_PREFS)
                .run {
                    NotificationsModel(
                        getBoolean(NEWS, false),
                        getBoolean(ARTICLES, false),
                        getBoolean(FAVORITE, false)
                    )
                }
        set(value) {
            with(value) {
                with(prefs(USER_PREFS)) {
                    edit()
                        .putBoolean(NEWS, news)
                        .putBoolean(ARTICLES, articles)
                        .putBoolean(FAVORITE, favorites)
                        .apply()
                }

            }
        }


    private fun prefs(name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)


    val tokenClients: MutableList<TokenChangeListener> = mutableListOf()
    val regionClients: MutableList<RegionChangeListener> = mutableListOf()

    interface TokenChangeListener {
        fun onTokenChanged(newToken: String)
    }

    interface RegionChangeListener {
        fun onRegionChanged(newRegionId: Int)
    }

}