package com.fsanchezdev.androidcomposeapp.presentationlayer

import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.domainlayer.base.DomainLayerContract
import com.fsanchezdev.androidcomposeapp.domainlayer.usecase.GetPlaceholderImageUseCase
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureEffectEvents
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureUserEvents
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.viewmodel.FeatureViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FeatureViewModelTest {

    private lateinit var viewModel: FeatureViewModel
    private lateinit var mockPlaceHolderRepository: DomainLayerContract.Data.PlaceHolderRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockPlaceHolderRepository = mockk(relaxed = true) // Inicializa el mock

        // Preparar el mock para devolver un resultado específico
        coEvery { mockPlaceHolderRepository.getPlaceHolderImage() } returns Either.Right(
            byteArrayOf()
        )

        // Inicializar el ViewModel con el UseCase y el Repository mockeados
        viewModel = FeatureViewModel(GetPlaceholderImageUseCase(mockPlaceHolderRepository))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Restablecer el Dispatcher principal
    }

    @Test
    fun `when OnButtonClicked event is sent, ShowGreetings effect is emitted`() = runTest {
        val events = mutableListOf<FeatureEffectEvents>()

        // Empezar a recoger los efectos en una coroutine
        val collectJob = launch(testDispatcher) {
            viewModel.effect.collect { events.add(it) }
        }

        // Acción: enviar el evento
        viewModel.onEvent(FeatureUserEvents.OnButtonClicked("Hi, Test!"))

        // Esperar a que se procesen las coroutines
        advanceUntilIdle()

        // Verificar que el efecto esperado se haya emitido
        assertTrue(
            events.contains(FeatureEffectEvents.ShowGreetings("Hi, Test!"))
        )

        collectJob.cancel() // Cancelar la recogida de efectos
    }
}
