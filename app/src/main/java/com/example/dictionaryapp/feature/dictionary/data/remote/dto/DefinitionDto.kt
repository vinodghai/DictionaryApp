package com.example.dictionaryapp.feature.dictionary.data.remote.dto

import com.example.dictionaryapp.feature.dictionary.domain.model.Definition


data class DefinitionDto(
    val antonyms: List<String>,
    val definition: String,
    val example: String?,
    val synonyms: List<String>
) {
    fun toDefinition(): Definition = Definition(
        definition = definition,
        antonyms = antonyms,
        example = example,
        synonyms = synonyms
    )
}

