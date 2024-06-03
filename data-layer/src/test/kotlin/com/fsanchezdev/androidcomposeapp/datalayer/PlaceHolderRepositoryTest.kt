package com.fsanchezdev.androidcomposeapp.datalayer

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.datalayer.base.DataLayerContract
import com.fsanchezdev.androidcomposeapp.datalayer.repository.PlaceHolderRepository
import com.fsanchezdev.androidcomposeapp.domainlayer.base.DomainLayerContract
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class PlaceHolderRepositoryTest {

    // Mock for the remote data source
    private lateinit var remoteDataSource: DataLayerContract.PlaceHolderDataSource.Remote

    // Instance of the class to be tested
    private lateinit var repository: DomainLayerContract.Data.PlaceHolderRepository

    @Before
    fun setUp() {
        // Initialize the mocks
        remoteDataSource = mockk()

        // Initialize the class to be tested
        repository = PlaceHolderRepository(remoteDataSource)
    }

    @Test
    fun testGetPlaceHolderImageReturnsDataWhenSourceSucceeds() = runTest {
        // Prepare
        val expectedData = ByteArray(10)
        coEvery { remoteDataSource.getPlaceHolderImage() } returns Either.Right(expectedData)

        // Execute
        val result = repository.getPlaceHolderImage()

        // Verify
        assertTrue(result.isRight())
        assertArrayEquals(expectedData, result.fold({ null }, { it }))
    }

    @Test
    fun testGetPlaceHolderImageReturnsFailureWhenSourceFails() = runTest {
        // Prepare
        val failure2 = FailureBo.NoNetwork
        coEvery { remoteDataSource.getPlaceHolderImage() } returns Either.Left(failure2)

        // Execute
        val result = repository.getPlaceHolderImage()

        // Verify
        assertTrue(result.isLeft())
        assertEquals(failure2, result.fold({ it }, { null }))
    }
}
