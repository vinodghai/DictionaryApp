package com.example.dictionaryapp.feature.dictionary.domain.model

data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String,
)