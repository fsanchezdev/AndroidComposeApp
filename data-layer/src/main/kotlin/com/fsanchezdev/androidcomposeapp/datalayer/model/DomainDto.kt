package com.fsanchezdev.androidcomposeapp.datalayer.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

public data class DefaultResponseDto
@OptIn(ExperimentalSerializationApi::class)
constructor(
    @JsonNames("code")
    val code: Int?,
    @JsonNames("description")
    val message: String,
    @JsonNames("status")
    val status: String? = null
)
