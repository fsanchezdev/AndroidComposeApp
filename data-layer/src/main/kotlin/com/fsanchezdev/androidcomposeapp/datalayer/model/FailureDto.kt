package com.fsanchezdev.androidcomposeapp.datalayer.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

/**
 * A class which models any failure coming from the 'data-layer'
 */
public sealed class FailureDto {
    public data object NoNetwork : FailureDto()
    public data object Unknown : FailureDto()
    public data object InputParamsError : FailureDto()
    public data object NoData : FailureDto()
    public class Unauthorized(public val code: String?, public val message: String?) : FailureDto()
    public class Forbidden(public val code: String?, public val message: String?) : FailureDto()
    public class ServerError(public val code: String?, public val message: String?) :
        FailureDto()
    public open class Error
    @OptIn(ExperimentalSerializationApi::class)
    constructor(
        @JsonNames("code") public val code: Int,
        @JsonNames("description") public val message: String?
    ) : FailureDto()

    public open class SpecificFailure<T>(public val error: T) : FailureDto()
}
