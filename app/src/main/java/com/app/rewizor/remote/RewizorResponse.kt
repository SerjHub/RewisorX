package com.app.rewizor.remote

import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.RewizorResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RewizorResponse<T> {
    @SerializedName("data")
    @Expose
    var data: T? = null

    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    fun isError() = code != 0

    fun map(default: T) =
        RewizorResult(
            data ?: default,
            if (isError()) RewizorError(code ?: -10, message ?: "Empty message")
            else null
        )

    override fun toString() =
        "code = $code :: message = $message :: data = $data"

}


