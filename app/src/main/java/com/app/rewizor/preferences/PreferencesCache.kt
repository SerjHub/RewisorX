package com.app.rewizor.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PreferencesCache(private val context: Application) {
    init {
        Log.i("FindBean", "pref")
    }
    companion object {
        const val APP_PREFS = "app"
        const val LOGIN = "login_pref"
        const val ACCESS_TOKEN = "token"
    }

    var sessionToken: String?
        get() = prefs(APP_PREFS)
            .getString(ACCESS_TOKEN, null)
        set(value) {
            prefs(APP_PREFS)
                .edit()
                .putString(ACCESS_TOKEN, value)
                .apply()
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

    private fun prefs(name: String): SharedPreferences =
        when (name) {
            APP_PREFS -> context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
            else  -> throw IllegalArgumentException("Put correct pref name in ${this::class.java.name}")
        }

}