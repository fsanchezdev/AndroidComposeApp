package com.fsanchezdev.androidcomposeapp.datalayer.di

import com.fsanchezdev.androidcomposeapp.datalayer.api.interceptor.ConnectivityInterceptorPlugin
import com.fsanchezdev.androidcomposeapp.datalayer.connectivity.ConnectivityDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
public object NetworkModule {

    /**
     * Provides an instance of [HttpClient] for making API requests.
     *
     * @param connectivityDataSource The data source that provides information about the device's network connectivity.
     * @return An instance of [HttpClient] with the necessary plugins and configurations.
     */
    @Provides
    @Singleton
    @Named("ApiClient")
    public fun provideApiHttpClient(connectivityDataSource: ConnectivityDataSource): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                Json {
                    // Include default property values in JSON output
                    encodeDefaults = true
                    // Ignore JSON fields that do not match any property in the Kotlin class
                    ignoreUnknownKeys = true
                    // Allow relaxed JSON parsing with non-standard syntax
                    isLenient = true
                }
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30000L
                connectTimeoutMillis = 30000L
                socketTimeoutMillis = 30000L
            }
            install(ConnectivityInterceptorPlugin) {
                configConnectivityDataSource = connectivityDataSource
            }
        }
    }
}
