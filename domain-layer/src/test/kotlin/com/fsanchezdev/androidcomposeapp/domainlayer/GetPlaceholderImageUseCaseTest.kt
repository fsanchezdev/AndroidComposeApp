package com.fsanchezdev.androidcomposeapp.domainlayer

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.domainlayer.base.DomainLayerContract
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import com.fsanchezdev.androidcomposeapp.domainlayer.usecase.GetPlaceholderImageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.fail

internal class GetPlaceholderImageUseCaseTest {

    // Mock of the repository
    private lateinit var placeHolderRepository: DomainLayerContract.Data.PlaceHolderRepository

    // Instance of the use case to be tested
    private lateinit var getPlaceholderImageUseCase: GetPlaceholderImageUseCase

    @Before
    fun setUp() {
        // Initialize the mock
        placeHolderRepository = mockk()
        // Initialize the use case with the mocked repository
        getPlaceholderImageUseCase = GetPlaceholderImageUseCase(placeHolderRepository)
    }

    @Test
    fun `invoke calls placeHolderRepository getPlaceHolderImage`() = runTest {
        // Prepare the mock to return a successful result
        coEvery { placeHolderRepository.getPlaceHolderImage() } returns Either.Right(byteArrayOf())

        // Call the invoke method of the use case
        getPlaceholderImageUseCase()

        // Verify that the repository was called
        coVerify(exactly = 1) { placeHolderRepository.getPlaceHolderImage() }
    }

    @Test
    fun `invoke returns success from placeHolderRepository`() = runTest {
        // A simulated successful result
        val expectedResult = byteArrayOf(1, 2, 3)
        coEvery { placeHolderRepository.getPlaceHolderImage() } returns Either.Right(expectedResult)

        // Execute the use case
        val result = getPlaceholderImageUseCase()

        // Verify that the result is the expected failure using fold
        result.fold(
            { fail("Expected a Right but got a Left") },
            { assertEquals(expectedResult, it) }
        )
    }

    @Test
    fun `invoke returns failure from placeHolderRepository`() = runTest {
        // A simulated failure result
        val expectedFailure = FailureBo.Unknown
        coEvery { placeHolderRepository.getPlaceHolderImage() } returns Either.Left(expectedFailure)

        // Execute the use case
        val result = getPlaceholderImageUseCase()

        // Verify that the result is the expected failure using fold
        result.fold(
            { assertEquals(expectedFailure, it) },
            { fail("Expected a Left but got a Right") }
        )
    }
}
