package com.fsanchezdev.androidcomposeapp.datalayer.datasource.placeholder

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.datalayer.api.ApiManager
import com.fsanchezdev.androidcomposeapp.datalayer.api.EndPoint
import com.fsanchezdev.androidcomposeapp.datalayer.base.DataLayerContract
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import io.ktor.client.call.body
import javax.inject.Inject

public class PlaceHolderRemoteDataSource @Inject constructor(
    private val apiManager: ApiManager
) : DataLayerContract.PlaceHolderDataSource.Remote {
    override suspend fun getPlaceHolderImage(): Either<FailureBo, ByteArray> =
        apiManager.get(EndPoint.GET_IMAGE.path).map { it.body<ByteArray>() }
}

// TODO, revistsar test,
//  revisar failure,
//  preview
