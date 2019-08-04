package com.app.rewizor.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.app.rewizor.App
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
    protected val asyncProvider = AsyncProvider(closeableAsyncTask)


    abstract fun onViewCreated()


    override fun onCleared() {
        super.onCleared()
        closeableAsyncTask.cancel()
    }


}