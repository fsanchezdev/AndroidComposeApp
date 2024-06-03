package com.fsanchezdev.androidcomposeapp.datalayer.api

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.datalayer.BuildConfig
import com.fsanchezdev.androidcomposeapp.datalayer.model.FailureDto
import com.fsanchezdev.androidcomposeapp.datalayer.model.toFailureBo
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.util.InternalAPI
import javax.inject.Inject
import javax.inject.Named

private const val HTTP_ERROR_402 = 402
private const val HTTP_ERROR_499 = 499

/**
 * Manages network requests for the application, abstracting the complexity of HTTP operations.
 * Utilizes Ktor's HttpClient for making requests and provides a unified approach to error handling.
 *
 * @property client The Ktor HttpClient, annotated to be provided via dependency injection.
 */
public class ApiManager @Inject constructor(
    @Named("ApiClient") public val client: HttpClient
) {

    public companion object {
        public const val URL: String = BuildConfig.BASE_URL
    }

    /**
     * Executes a GET request to the specified endpoint.
     *
     * @param endPoint The API endpoint to which the GET request is made.
     * @param request Custom HttpRequestBuilder to allow customization of the request.
     * @return [Either] A wrapper that holds either a successful [HttpResponse] or a [FailureBo].
     */
    public suspend inline fun get(
        endPoint: String,
        request: HttpRequestBuilder = request {}
    ): Either<FailureBo, HttpResponse> {
        return validate {
            val response: HttpResponse =
                client.get(URL + endPoint) {
                    headers {
                        request.headers
                    }
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            Either.Right(response)
        }.mapLeft { failure ->
            failure.toFailureBo()
        }
    }

    /**
     * Executes a POST request to the specified endpoint.
     *
     * @param endPoint The API endpoint to which the POST request is made.
     * @param request Custom HttpRequestBuilder to allow customization of the request, including the request body.
     * @return [Either] A wrapper that holds either a successful [HttpResponse] or a [FailureBo].
     */
    @OptIn(InternalAPI::class)
    public suspend inline fun <reified T> post(
        endPoint: String,
        request: HttpRequestBuilder = request {}
    ): Either<FailureBo, HttpResponse> {
        return validate {
            val response: HttpResponse =
                client.post(URL + endPoint) {
                    headers {
                        request.headers
                    }
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    body = request.body
                }
            Either.Right(response)
        }.mapLeft { failure ->
            failure.toFailureBo()
        }
    }

    /**
     * Executes a PUT request to the specified endpoint.
     *
     * @param endPoint The API endpoint to which the PUT request is made.
     * @param request Custom HttpRequestBuilder to allow customization of the request, including the request body.
     * @return [Either] A wrapper that holds either a successful [HttpResponse] or a [FailureBo].
     */
    @OptIn(InternalAPI::class)
    public suspend inline fun put(
        endPoint: String,
        request: HttpRequestBuilder = request {}
    ): Either<FailureBo, HttpResponse> {
        return validate {
            val response: HttpResponse =
                client.put(URL + endPoint) {
                    headers {
                        request.headers
                    }
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    body = request.body
                }
            Either.Right(response)
        }.mapLeft { failure ->
            failure.toFailureBo()
        }
    }

    /**
     * Executes a PATCH request to the specified endpoint.
     *
     * @param endPoint The API endpoint to which the PATCH request is made.
     * @param request Custom HttpRequestBuilder to allow customization of the request, including the request body.
     * @return [Either] A wrapper that holds either a successful [HttpResponse] or a [FailureBo].
     */
    @OptIn(InternalAPI::class)
    public suspend inline fun patch(
        endPoint: String,
        request: HttpRequestBuilder = request {}
    ): Either<FailureBo, HttpResponse> {
        return validate {
            val response: HttpResponse =
                client.patch(URL + endPoint) {
                    headers {
                        request.headers
                    }
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    body = request.body
                }
            Either.Right(response)
        }.mapLeft { failure ->
            failure.toFailureBo()
        }
    }

    /**
     * Executes a DELETE request to the specified endpoint.
     *
     * @param endPoint The API endpoint to which the DELETE request is made.
     * @param request Custom HttpRequestBuilder to allow customization of the request.
     * @return [Either] A wrapper that holds either a successful [HttpResponse] or a [FailureBo].
     */
    public suspend inline fun delete(
        endPoint: String,
        request: HttpRequestBuilder = request {}
    ): Either<FailureBo, HttpResponse> {
        return validate {
            val response: HttpResponse =
                client.delete(URL + endPoint) {
                    headers {
                        request.headers
                    }
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            Either.Right(response)
        }.mapLeft { failure ->
            failure.toFailureBo()
        }
    }

    /**
     * Validates a network call, handling any errors and converting them into application-specific failures.
     * This method abstracts the error handling logic for all network requests made through this manager.
     *
     * @param T The type parameter indicating the success return type.
     * @param apiCall The suspend function representing the API call to be validated.
     * @return [Either] A wrapper that holds either a successful result of type [T] or a [FailureDto].
     */
    public suspend fun <T> validate(
        apiCall: suspend () -> Either<FailureDto, T>
    ): Either<FailureDto, T> {
        return try {
            apiCall()
        } catch (t: Throwable) {
            print(t)
            when (t) {
                is ClientRequestException ->
                    when (t.response.status.value) {
                        HttpStatusCode.Unauthorized.value -> {
                            Either.Left(
                                FailureDto.Unauthorized(
                                    t.response.status.value.toString(),
                                    t.message
                                )
                            )
                        }
                        HttpStatusCode.BadRequest.value, in HTTP_ERROR_402..HTTP_ERROR_499 -> {
                            Either.Left(
                                FailureDto.Forbidden(t.response.status.value.toString(), t.message)
                            )
                        }
                        else -> {
                            Either.Left(
                                FailureDto.ServerError(
                                    t.response.status.value.toString(),
                                    t.message
                                )
                            )
                        }
                    }
                is ServerResponseException -> { // 5xx
                    Either.Left(
                        FailureDto.ServerError(t.response.status.value.toString(), t.message)
                    )
                }
                else -> {
                    Either.Left(FailureDto.Unknown)
                }
            }
        }
    }
}
