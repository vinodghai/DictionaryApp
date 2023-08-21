package com.example.dictionaryapp.feature.dictionary.data.remote.dto

import com.example.dictionaryapp.feature.dictionary.data.local.entity.WordInfoEntity


data class WordInfoDto(
    val licenseDto: LicenseDto,
    val meanings: List<MeaningDto>,
    val phonetic: String?,
    val phonetics: List<PhoneticDto>,
    val sourceUrls: List<String>,
    val word: String
) {

    fun toWordInfoEntity(): WordInfoEntity = WordInfoEntity(
        meanings = meanings.map { it.toMeaning() },
        word = word,
        phonetic = phonetic,
        origin = sourceUrls.first(),
    )
}