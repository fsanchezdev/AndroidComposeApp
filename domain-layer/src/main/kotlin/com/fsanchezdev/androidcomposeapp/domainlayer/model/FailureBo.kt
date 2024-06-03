package com.fsanchezdev.androidcomposeapp.domainlayer.model

import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR

/**
 * A class which models any failure coming from the 'domain-layer'
 */
public sealed class FailureBo {
    public data object NoNetwork : FailureBo()
    public data object Unknown : FailureBo()
    public data object InputParamsError : FailureBo()
    public data object NoData : FailureBo()
    public class Unauthorized(public val code: String?, public val message: String?) : FailureBo()
    public class Forbidden(public val code: String?, public val message: String?) : FailureBo()
    public class ServerError(public val code: String?, public val message: String?) :
        FailureBo()
    public open class Error(public val code: Int = HTTP_INTERNAL_ERROR, public val message: String?) : FailureBo()
    public open class ErrorResId(public val code: Int = HTTP_INTERNAL_ERROR, public val resId: Int) : FailureBo()
    public open class SpecificFailure<T>(public val error: T) : FailureBo()
}
