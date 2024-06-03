package com.fsanchezdev.androidcomposeapp.domainlayer.usecase

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.domainlayer.base.DomainLayerContract
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import javax.inject.Inject

public class GetPlaceholderImageUseCase @Inject constructor(
    private val placeHolderRepository: DomainLayerContract.Data.PlaceHolderRepository
) {
    public suspend operator fun invoke(): Either<FailureBo, ByteArray> =
        placeHolderRepository.getPlaceHolderImage()
}
