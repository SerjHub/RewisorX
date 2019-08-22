package com.app.rewizor.viewmodel.model

sealed class ToolBarModel(open val title: String)
class Login(title: String): ToolBarModel(title)
class Registration(title: String, val previousView: String): ToolBarModel(title) {
    companion object {
        const val MAIN_MENU = "menu"
        const val LOGIN = "login"
    }
}
class Recover(title: String): ToolBarModel(title)