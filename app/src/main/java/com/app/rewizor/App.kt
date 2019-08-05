package com.app.rewizor

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application(){
    

    override fun onCreate() {
        super.onCreate()
        startKoin{
            listOf(
                androidContext(this@App).modules(modules)

            )
        }
    }

    private val modules = listOf(
        androidModule,
        apiModule,
        sources,
        dataModule,
        viewModelModule
    )
}