package com.app.rewizor.global

import android.app.Application
import com.app.rewizor.global.*
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