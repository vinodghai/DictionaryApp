package com.example.dictionaryapp.feature.dictionary.data.remote.dto


data class PhoneticDto(
    val audio: String,
    val licenseDto: LicenseDto,
    val sourceUrl: String,
    val text: String
)