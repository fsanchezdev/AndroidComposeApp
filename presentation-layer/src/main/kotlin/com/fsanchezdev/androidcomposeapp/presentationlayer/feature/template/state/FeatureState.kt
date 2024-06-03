package com.fsanchezdev.androidcomposeapp.presentationlayer.feature.template.state

import android.graphics.Bitmap
import com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose.BaseMviState
import com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose.ComposeStateType

public data class FeatureState(
    val stateGreeting: String = "Hello, World!",
    val image: Bitmap? = null,
    override var type: ComposeStateType = ComposeStateType.Render
) : BaseMviState(type)
