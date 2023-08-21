package com.example.dictionaryapp.feature.dictionary.domain.repository

import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.feature.dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {

    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>
}