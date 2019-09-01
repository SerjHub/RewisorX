package com.app.rewizor.exstension

import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.processWith
import com.app.rewizor.viewmodel.BaseViewModel


fun <T> BaseViewModel.asyncRequest(
    dataProviderResult: suspend () -> RewizorResult<T>,
    onFail: (error: RewizorError) -> Unit,
    onSuccess: (data: T?) -> Unit
) {
    with(asyncProvider) {
        startSuspend {
            executeBackGroundTask {
                dataProviderResult.invoke()
            }.processWith(
                { onFail.invoke(it) },
                { onSuccess.invoke(it) }
            )
        }
    }
}