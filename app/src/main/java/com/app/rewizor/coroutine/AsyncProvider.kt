package com.app.rewizor.coroutine

import com.app.rewizor.data.Result
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AsyncProvider (uiComponentContext: CoroutineContext): CoroutineScope {


    override val coroutineContext = uiComponentContext + Dispatchers.Main

    fun startSuspend(bgTask: suspend CoroutineScope.() -> Unit) = launch { bgTask() }

    @Synchronized
    suspend fun <T>startBlockingOperationWithResultAsync(block: suspend CoroutineScope.() -> Result<T>): Deferred<Result<T>> =
        coroutineScope { async(Dispatchers.IO) { block()} }

    @Synchronized
    suspend fun <T>startBlockingOperationWithResultAsyncAwait(block: suspend CoroutineScope.() -> Result<T>): Result<T> =
        startBlockingOperationWithResultAsync(block).await()
}