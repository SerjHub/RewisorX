package com.app.rewizor


import com.app.rewizor.data.repository.LoginRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.data.repositoryImpl.LoginRepositoryImpl
import com.app.rewizor.data.repositoryImpl.SystemRepositoryImpl
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.remote.RestClient
import com.app.rewizor.viewmodel.LoginViewModel
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

val sources = module {
    single { PreferencesCache(androidContext() as App) }
}

val apiModule = module {
    single { RestClient(get(), androidContext().resources.getString(R.string.rewizor_url)) }
}

/**repositories */
val dataModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get(), get()) }
    single<SystemRepository> { SystemRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { StartViewModel(get(), get()) }
    viewModel { LoginViewModel(get(),get()) }
}




