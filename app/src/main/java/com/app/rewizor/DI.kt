package com.app.rewizor


import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router


val androidModule = module {
single { Cicerone.create() }
}

val apiModule = module {

}

val dataModule = module {

}
