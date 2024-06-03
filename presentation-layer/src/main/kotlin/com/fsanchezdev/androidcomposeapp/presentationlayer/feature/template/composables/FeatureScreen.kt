package com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import com.fsanchezdev.androidcomposeapp.presentationlayer.R
import com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose.utils.HandleFailure
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureEffectEvents
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureState
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state.FeatureUserEvents
import com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.viewmodel.FeatureViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

@Composable
public fun FeatureScreen() {
    val vm = hiltViewModel<FeatureViewModel>()
    val state: FeatureState by vm.screenState

    FeatureScreen(
        state = state,
        onEvent = vm::onEvent,
        effect = vm.effect,
        failure = vm.failure
    )
}

@Composable
public fun FeatureScreen(
    state: FeatureState,
    onEvent: (FeatureUserEvents) -> Unit,
    effect: Flow<FeatureEffectEvents>,
    failure: SharedFlow<FailureBo>
) {
    var fieldText: String by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        effect.onEach { effect ->
            when (effect) {
                is FeatureEffectEvents.ShowGreetings -> {
                    fieldText = "from effect ${effect.greeting}"
                    // used for check the effect, in this case we could use variable fieldtext only
                }
            }
        }.collect()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.greeter),
            modifier = Modifier
                .padding(16.dp)
                .size(30.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            Text(text = stringResource(R.string.insert_name))
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.semantics {
                    contentDescription = "Insert name"
                }
            )
            Button(
                onClick = {
                    onEvent(FeatureUserEvents.OnButtonClicked(text))
                    state.stateGreeting
                },
                modifier = Modifier.semantics {
                    contentDescription = "Greet button"
                }
            ) {
                Text(text = stringResource(R.string.greet))
            }
            Text(
                text = fieldText,
                modifier = Modifier.semantics {
                    contentDescription = "Greeting"
                }
            )
            /*
            AsyncImage(
                model = "https://placehold.co/600x400",
                contentDescription = null,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            )
             */
            state.image?.asImageBitmap()?.let { Image(bitmap = it, contentDescription = "") }
        }

        HandleFailure(
            failure = failure,
            onEvent = { onEvent(FeatureUserEvents.OnNoNetworkRetryClick) }
        )
    }
}

@Preview(showBackground = true, name = "Day mode")
@Preview(showBackground = true, name = "Night mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    FeatureScreen(
        state = FeatureState(),
        onEvent = {},
        effect = flowOf(),
        failure = MutableSharedFlow()
    )
}
