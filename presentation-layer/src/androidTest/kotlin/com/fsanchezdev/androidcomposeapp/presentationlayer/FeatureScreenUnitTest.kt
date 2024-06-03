package com.fsanchezdev.androidcomposeapp.presentationlayer

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import arrow.core.Either
import com.fsanchezdev.androidcomposeapp.domainlayer.base.DomainLayerContract
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import com.fsanchezdev.androidcomposeapp.domainlayer.usecase.GetPlaceholderImageUseCase
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.composables.FeatureScreen
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureState
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.viewmodel.FeatureViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class FeatureScreenUnitTest {
    @get:Rule
    internal val composeTestRule = createComposeRule()

    private lateinit var mockPlaceHolderRepository: DomainLayerContract.Data.PlaceHolderRepository
    private lateinit var viewModel: FeatureViewModel

    @Before
    fun setup() {
        mockPlaceHolderRepository = mockk(relaxed = true)
        coEvery { mockPlaceHolderRepository.getPlaceHolderImage() } returns Either.Right(
            byteArrayOf()
        )
        viewModel = FeatureViewModel(GetPlaceholderImageUseCase(mockPlaceHolderRepository))
    }

    @Test
    fun featureScreen_updatesTextOnButtonCLick() = runTest {
        val failureFlow = MutableSharedFlow<FailureBo>()

        composeTestRule.setContent {
            FeatureScreen(
                state = FeatureState(stateGreeting = ""),
                onEvent = viewModel::onEvent,
                effect = viewModel.effect,
                failure = failureFlow
            )
        }

        composeTestRule.onNodeWithContentDescription("Insert name")
            .performTextInput("Mario")
        composeTestRule.onNodeWithContentDescription("Greet button")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Greeting")
            .assertTextEquals("from effect Mario")
    }
}
