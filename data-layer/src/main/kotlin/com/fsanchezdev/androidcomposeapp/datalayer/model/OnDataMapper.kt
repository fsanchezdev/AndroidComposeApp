package com.fsanchezdev.androidcomposeapp.datalayer.model

import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import java.net.HttpURLConnection

/**
 * Maps a [FailureDto] into a [FailureBo]
 */
public fun FailureDto.toFailureBo(): FailureBo = when (this) {
    FailureDto.NoNetwork -> FailureBo.NoNetwork
    FailureDto.InputParamsError -> FailureBo.InputParamsError
    FailureDto.NoData -> FailureBo.NoData
    is FailureDto.Forbidden -> FailureBo.Forbidden(code = code, message = message)
    is FailureDto.ServerError -> FailureBo.ServerError(code = code, message = message)
    is FailureDto.Unauthorized -> FailureBo.Unauthorized(code = code, message = message)
    FailureDto.Unknown -> FailureBo.Unknown
    is FailureDto.Error -> {
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            FailureBo.Unauthorized(code = code.toString(), message = message)
        } else {
            FailureBo.Error(code = code, message = message)
        }
    }
    is FailureDto.SpecificFailure<*> -> FailureBo.SpecificFailure(error = error)
}
