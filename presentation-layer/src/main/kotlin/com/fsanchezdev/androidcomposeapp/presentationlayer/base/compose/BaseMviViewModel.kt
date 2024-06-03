package com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

public abstract class BaseMviViewModel<
    User : BaseMviEvent.User,
    Effect : BaseMviEvent.Effect,
    UiState : BaseMviState
    >(
    initialState: UiState
) : ViewModel() {

    private val _screenState: MutableState<UiState> = mutableStateOf(value = initialState)
    public val screenState: State<UiState>
        get() = _screenState

    private val _effect: Channel<Effect> = Channel()
    public val effect: Flow<Effect>
        get() = _effect.receiveAsFlow()

    private val _failure = MutableSharedFlow<FailureBo>()
    public val failure: SharedFlow<FailureBo>
        get() = _failure.asSharedFlow()

    public abstract fun onEvent(event: User)

    protected fun emitState(state: UiState.() -> UiState) {
        _screenState.value = state.invoke(screenState.value)
    }

    protected fun sendEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    protected fun CoroutineScope.launchFailure(failure: FailureBo): Job = launch {
        emitFailure(failure)
    }

    protected suspend fun emitFailure(failure: FailureBo): Unit = _failure.emit(failure)
}
