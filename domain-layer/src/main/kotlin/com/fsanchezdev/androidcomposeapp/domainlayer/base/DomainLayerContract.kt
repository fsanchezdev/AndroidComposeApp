package com.fsanchezdev.androidcomposeapp.domainlayer.base

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo

/**
 * Contract defining the domain layer structure of the application. This contract is designed
 * to outline how repositories should be structured and accessed within the app.
 */
public interface DomainLayerContract {
    public interface Data {
        public interface PlaceHolderRepository {
            public suspend fun getPlaceHolderImage(): Either<FailureBo, ByteArray>
        }
    }
}
