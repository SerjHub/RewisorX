package com.app.rewizor


import com.app.rewizor.data.repository.*
import com.app.rewizor.data.repositoryImpl.*
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.remote.RestClient
import com.app.rewizor.viewmodel.*
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
    single { RestClient(androidContext().resources.getString(R.string.rewizor_url), get()) }
}

/**repositories */
val dataModule = module {
    single<AccountRepository> { AccountRepositoryImpl(get(), get()) }
    single<LoginRepository> { LoginRepositoryImpl(get(), get(), get()) }
    single<SystemRepository> { SystemRepositoryImpl(get()) }
    single<RegistrationRepository> { RegistrationRepositoryImpl(get(), get()) }
    single<PublicationRepository> { PublicationRepositoryImpl(get()) }
}

val viewModelModule = module {
    single { StartViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegistrationViewModel(get(), get()) }
    viewModel { RecoverPasswordViewModel(get()) }
    viewModel { CategoryListViewModel(get()) }
    viewModel { TopicViewModel(get()) }
}




