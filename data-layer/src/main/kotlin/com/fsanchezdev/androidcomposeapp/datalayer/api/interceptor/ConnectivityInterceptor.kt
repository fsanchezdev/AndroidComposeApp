package com.fsanchezdev.androidcomposeapp.datalayer.api.interceptor

import com.fsanchezdev.androidcomposeapp.datalayer.connectivity.ConnectivityDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpSendPipeline
import io.ktor.util.AttributeKey
import java.io.IOException
import javax.inject.Inject

/**
 * This interceptor is used for check network and throws a custom exception is needed
 */
public class ConnectivityInterceptorPlugin @Inject constructor(
    public val connectivityDataSource: ConnectivityDataSource
) {
    public class Configuration {
        public lateinit var configConnectivityDataSource: ConnectivityDataSource
    }

    public companion object Feature :
        HttpClientPlugin<Configuration, ConnectivityInterceptorPlugin> {
        public override val key: AttributeKey<ConnectivityInterceptorPlugin> =
            AttributeKey("ConnectivityInterceptorPlugin")

        public override fun prepare(
            block: Configuration.() -> Unit
        ): ConnectivityInterceptorPlugin {
            val config = Configuration().apply(block)
            return ConnectivityInterceptorPlugin(config.configConnectivityDataSource)
        }

        public override fun install(plugin: ConnectivityInterceptorPlugin, scope: HttpClient) {
            scope.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                if (!plugin.connectivityDataSource.checkNetworkConnectionAvailability()) {
                    throw NoConnectivityException()
                }
            }
        }
    }
}

/**
 * Custom [IOException] for no connectivity.
 */
public class NoConnectivityException : IOException("No network connectivity")
