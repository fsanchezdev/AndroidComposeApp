package com.fsanchezdev.androidcomposeapp.datalayer.connectivity

import android.content.Context
import com.fsanchezdev.androidcomposeapp.datalayer.utils.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

public class ConnectivityDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectivityDataSource {
    override fun checkNetworkConnectionAvailability(): Boolean = context.isNetworkAvailable()
}
