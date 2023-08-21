package com.example.dictionaryapp.feature.dictionary.presentation

import com.example.dictionaryapp.feature.dictionary.domain.model.WordInfo

data class WordInfoState(
    val wordInfoItems: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
