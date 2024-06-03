package com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.fsanchezdev.androidcomposeapp.domainlayer.model.FailureBo
import com.fsanchezdev.androidcomposeapp.presentationlayer.R
import com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose.composables.AlertDialogError
import com.fsanchezdev.androidcomposeapp.presentationlayer.base.compose.composables.DialogFullScreen
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

/**
 * Handle failures
 *
 * @param failure
 * @param onEvent
 */
@Composable
public fun HandleFailure(failure: SharedFlow<FailureBo>, onEvent: () -> Unit) {
    val context = LocalContext.current
    var openAlertDialog by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    val title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        failure.onEach { failure ->
            when (failure) {
                FailureBo.NoNetwork -> {
                    openDialog = true
                }
                FailureBo.Unknown -> {
                    message = context.getString(R.string.unknown_error)
                    openAlertDialog = true
                }
                FailureBo.InputParamsError -> {
                    message = context.getString(R.string.error_input_params)
                    openAlertDialog = true
                }
                FailureBo.NoData -> {}
                is FailureBo.Unauthorized -> {
                    message = context.getString(R.string.unauthorized_error)
                    openAlertDialog = true
                }
                is FailureBo.ServerError -> {
                    message = context.getString(R.string.server_error)
                    openAlertDialog = true
                }
                is FailureBo.Forbidden -> {
                    message = context.getString(R.string.unauthorized_error)
                    openAlertDialog = true
                }
                is FailureBo.Error -> {
                    message = failure.message.toString()
                    openAlertDialog = true
                }
                is FailureBo.ErrorResId -> {
                    message = context.getString(failure.resId)
                    openAlertDialog = true
                }
                is FailureBo.SpecificFailure<*> -> {}
            }
        }.collect()
    }

    if (openAlertDialog) {
        AlertDialogError(
            title = title.ifEmpty { LocalContext.current.getString(R.string.error) },
            message = message,
            onConfirm = {}
        )
    }

    if (openDialog) {
        DialogFullScreen(isDisplayed = true, onEvent = onEvent)
    }
}
