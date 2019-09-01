package com.app.rewizor.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.app.rewizor.data.repositoryImpl.AccountRepositoryImpl

class PreferencesCache(private val context: Application) {

    companion object {
        const val APP_PREFS = "app"
        const val LOGIN = "login_pref"
        const val ACCESS_TOKEN = "token"
        const val SAVED_REGION = "region_id"

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


    private fun prefs(name: String): SharedPreferences =
        when (name) {
            APP_PREFS -> context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
            else  -> throw IllegalArgumentException("Put correct pref name in ${this::class.java.name}")
        }

    val tokenClients: MutableList<TokenChangeListener> = mutableListOf()
    val regionClients: MutableList<RegionChangeListener> = mutableListOf()

    interface TokenChangeListener {
        fun onTokenChanged(newToken: String)
    }
    interface RegionChangeListener {
        fun onRegionChanged(newRegionId: Int)
    }

}