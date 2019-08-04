package com.app.rewizor


import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.data.repositoryImpl.LoginRepositoryImpl
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.viewmodel.StartViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router


val androidModule = module { 
    single { Cicerone.create() }
    single { Router() }
}

val viewModelModule = module {
    viewModel { StartViewModel() }
}


val apiModule = module {

}

val dataModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
}

val sources = module {
    single { PreferencesCache(androidContext() as App) }
}
