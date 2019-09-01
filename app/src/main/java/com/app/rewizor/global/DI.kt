package com.app.rewizor.global


import com.app.rewizor.R
import com.app.rewizor.data.repository.*
import com.app.rewizor.data.repositoryImpl.*
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.remote.RestClient
import com.app.rewizor.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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
    single<SystemRepository> { SystemRepositoryImpl(get(), get(), get()) }
    single<RegistrationRepository> { RegistrationRepositoryImpl(get(), get()) }
    single<PublicationRepository> { PublicationRepositoryImpl(get(), get()) }
}

val viewModelModule = module {
    single { AuthorizationViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { RecoverPasswordViewModel(get()) }
    viewModel { CategoryListViewModel(get()) }
    viewModel { TopicViewModel(get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { RegionViewModel(get(), get()) }
}




