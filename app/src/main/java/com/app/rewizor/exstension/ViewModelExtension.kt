package com.app.rewizor.exstension

import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.processWith
import com.app.rewizor.viewmodel.BaseViewModel


fun <T> BaseViewModel.asyncRequest(
    loadData: suspend () -> RewizorResult<T>,
    onFail: (error: RewizorError) -> Unit,
    onSuccess: (data: T?) -> Unit,
    postOnStart: PostBlock? = null,
    postOnFinish: PostBlock? = null
) {
    with(asyncProvider) {
        startSuspend {
            executeBackGroundTask {
                loadData.invoke() }
                .processWith(
                    { onFail.invoke(it) },
                    { onSuccess.invoke(it) })
                .also {
                    postOnFinish?.invoke()
                }
        }
    }
}

typealias PostBlock = () -> Unit