package com.example.dictionaryapp.core.util

import com.example.dictionaryapp.feature.dictionary.domain.model.UIError

sealed interface Resource<T> {
    class Loading<T>(val data: T? = null) : Resource<T>
    class Success<T>(val data: T) : Resource<T>
    class Error<T>(val uiError: UIError) : Resource<T>
}