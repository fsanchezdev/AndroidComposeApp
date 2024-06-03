package com.fsanchezdev.androidcomposeapp.datalayer.base

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo

/**
 * Contract defining the data layer structure of the application. This contract is designed
 * to outline how data sources should be structured and accessed within the app.
 */
public interface DataLayerContract {
    public interface PlaceHolderDataSource {
        public interface Remote {
            public suspend fun getPlaceHolderImage(): Either<FailureBo, ByteArray>
        }
    }
}
