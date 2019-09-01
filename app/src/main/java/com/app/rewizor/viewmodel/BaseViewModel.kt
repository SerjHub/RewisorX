package com.app.rewizor.viewmodel

import androidx.lifecycle.ViewModel
import com.app.rewizor.coroutine.AsyncProvider
import kotlinx.coroutines.Job
import org.koin.core.KoinComponent
import org.koin.core.get
import ru.terrakok.cicerone.Router

abstract class BaseViewModel : ViewModel(), KoinComponent {
    private val closeableAsyncTask = Job()
    /** for navigation between fragments */
    protected val router: Router = get()
    /** for background work */
    val asyncProvider = AsyncProvider(closeableAsyncTask)


    abstract fun onViewCreated()


    override fun onCleared() {
        super.onCleared()
        closeableAsyncTask.cancel()
    }


}