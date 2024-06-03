package com.fsanchezdev.androidcomposeapp.datalayer.di

import com.fsanchezdev.androidcomposeapp.datalayer.repository.PlaceHolderRepository
import com.fsanchezdev.androidcomposeapp.domainlayer.base.DomainLayerContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
public abstract class RepositoryModule {
    @Binds
    public abstract fun bindPlaceHolderRepository(
        placeHolderRepository: PlaceHolderRepository
    ): DomainLayerContract.Data.PlaceHolderRepository
}
