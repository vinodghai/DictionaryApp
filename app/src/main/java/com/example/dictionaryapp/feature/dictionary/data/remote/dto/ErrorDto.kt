package com.example.dictionaryapp.feature.dictionary.data.remote.dto

import com.example.dictionaryapp.feature.dictionary.domain.model.UIError


data class ErrorDto(
    val message: String,
    val resolution: String,
    val title: String
) {
    fun toError(): UIError = UIError(
        message = message,
        resolution = resolution,
        title = title
    )
}