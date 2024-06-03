package com.fsanchezdev.androidcomposeapp.datalayer.repository

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.datalayer.base.DataLayerContract
import com.fsanchezdev.androidcomposeapp.domainlayer.base.DomainLayerContract
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import javax.inject.Inject

public class PlaceHolderRepository @Inject constructor(
    private val placeHolderDataSource: DataLayerContract.PlaceHolderDataSource.Remote
) : DomainLayerContract.Data.PlaceHolderRepository {
    override suspend fun getPlaceHolderImage(): Either<FailureBo, ByteArray> =
        placeHolderDataSource.getPlaceHolderImage()
}
