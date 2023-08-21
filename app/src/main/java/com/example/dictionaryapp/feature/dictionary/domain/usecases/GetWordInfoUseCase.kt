package com.example.dictionaryapp.feature.dictionary.domain.usecases

import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.feature.dictionary.domain.model.WordInfo
import com.example.dictionaryapp.feature.dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWordInfoUseCase(
    private val repository: WordInfoRepository
) {

    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> =
        if (word.isBlank()) flow {  } else repository.getWordInfo(word)
}