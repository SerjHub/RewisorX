package com.app.rewizor.remote

import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.RewizorResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RewizorResponse<T> {
    @SerializedName("data")
    @Expose
    val data: T? = null

    @SerializedName("status")
    @Expose
    val status: Int? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    val isError
        get() = status != 0

    fun map(default: T) =
        RewizorResult(
            data ?: default,
            if (isError) RewizorError(status ?: -10, message ?: "Empty message")
            else null
        )

    override fun toString() =
        "status = $status :: message = $message :: data = $data"

}


