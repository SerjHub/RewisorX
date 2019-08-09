package com.app.rewizor.coroutine

import com.app.rewizor.data.RewizorResult
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AsyncProvider (uiComponentContext: CoroutineContext): CoroutineScope {


    override val coroutineContext = uiComponentContext + Dispatchers.Main

    fun startSuspend(bgTask: suspend CoroutineScope.() -> Unit) = launch { bgTask() }

    @Synchronized
    suspend fun <T>startBlockingOperationWithResultAsync(block: suspend CoroutineScope.() -> RewizorResult<T>): Deferred<RewizorResult<T>> =
        coroutineScope { async(Dispatchers.IO) { block()} }

    @Synchronized
    suspend fun <T>executeBackGroundTask(block: suspend CoroutineScope.() -> RewizorResult<T>): RewizorResult<T> =
        startBlockingOperationWithResultAsync(block).await()
}