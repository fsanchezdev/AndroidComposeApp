package com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.viewmodel

import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import com.fsanchezdev.androidcomposeapp.domainlayer.usecase.GetPlaceholderImageUseCase
import com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose.BaseMviViewModel
import com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose.ComposeStateType
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureEffectEvents
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureState
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureUserEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class FeatureViewModel @Inject constructor(
    getPlaceholderImageUseCase: GetPlaceholderImageUseCase
) : BaseMviViewModel<
    FeatureUserEvents,
    FeatureEffectEvents,
    FeatureState
    >(
    initialState = FeatureState()
) {
    init {
        viewModelScope.launch {
            getPlaceholderImageUseCase()
                .fold(::handleError, ::handleGetImageSuccess)
        }
    }
    override fun onEvent(event: FeatureUserEvents) {
        when (event) {
            is FeatureUserEvents.OnButtonClicked -> sendEffect {
                FeatureEffectEvents.ShowGreetings(event.buttonTextGreeting)
            }

            FeatureUserEvents.OnNoNetworkRetryClick -> {}
        }
    }

    private fun handleGetImageSuccess(image: ByteArray) {
        val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        emitState { copy(image = imageBitmap, type = ComposeStateType.Render) }
    }

    private fun handleError(error: FailureBo) {
        emitState { copy(type = ComposeStateType.Render) }
        viewModelScope.launchFailure(error)
    }
}
