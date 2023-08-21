package com.example.dictionaryapp.feature.dictionary.data.repository

import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.core.util.SafeApiCall
import com.example.dictionaryapp.feature.dictionary.data.local.WordInfoDao
import com.example.dictionaryapp.feature.dictionary.data.remote.DictionaryApi
import com.example.dictionaryapp.feature.dictionary.domain.model.WordInfo
import com.example.dictionaryapp.feature.dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao,
    private val safeApiCall: SafeApiCall
) : WordInfoRepository {
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> {
        return flow {
            emit(Resource.Loading())
            val wordInfoList = dao.getWordInfos(word)
            emit(Resource.Loading(wordInfoList.map { it.toWordInfo() }))
            when (val apiResponse = safeApiCall { api.getWordInfo(word) }) {
                is Resource.Success -> {
                    dao.deleteWordInfos(apiResponse.data.map { it.word })
                    dao.insertWordInfos(apiResponse.data.map { it.toWordInfoEntity() })
                    emit(Resource.Success(dao.getWordInfos(word).map { it.toWordInfo() }))
                }

                is Resource.Error -> {
                    emit(Resource.Error(apiResponse.uiError))
                }

                else -> {

                }
            }
        }
    }
}