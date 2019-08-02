package com.app.rewizor.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class AsyncProvider (uiComponentContext: CoroutineContext): CoroutineScope {


    override val coroutineContext = uiComponentContext + Dispatchers.Main

    fun <T>startBlockingOperationWithResult(block: CoroutineScope.() -> Result<T>) =
        launch { withContext(Dispatchers.IO) { block() } }

}